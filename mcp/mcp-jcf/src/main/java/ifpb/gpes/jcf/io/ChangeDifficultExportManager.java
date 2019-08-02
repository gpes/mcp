package ifpb.gpes.jcf.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ifpb.gpes.Call;
import ifpb.gpes.ExportManager;
import ifpb.gpes.filter.FilterClassType;

import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ChangeDifficultExportManager extends ExportManager {

    private String FILENAME = "metrics.json";

    public ChangeDifficultExportManager(String outputDir) {
        super(outputDir);
    }

    @Override
    public void export(List<Call> elements) {
        // load the json categorization used to indentify the category of an method
        JsonNode categoriesNode = new JsonFile(getClass().getClassLoader().getResourceAsStream("categories.json")).toJsonObject();
        //
        ObjectNode result = new ObjectMapper().createObjectNode();
        //
        Arrays.asList("java.util.List", "java.util.Map", "java.util.Set").forEach(interfaceFullName -> {
            Predicate<Call> predicate = new FilterClassType(interfaceFullName);
            List<Call> calls = elements.stream().filter(predicate).collect(Collectors.toList());
            Map<String, Set<String>> categoriesMethodsMap = classifyCallsByCategories(calls, categoriesNode);
            // get interface simple name
            String[] split = interfaceFullName.split("\\.");
            String interfaceSimpleName = split[split.length-1];
            //
            ArrayNode nodes = calculateChangeMetricByCategoriesAndInterface(categoriesMethodsMap, categoriesNode, interfaceSimpleName);
            //
            result.set(interfaceFullName, nodes);
        });
        try {
            String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(result);
            write(json, Paths.get(handleOutputFilePath(".", outputDir+"-"+FILENAME)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Set<String>> classifyCallsByCategories(List<Call> calls, JsonNode categoriesNode) {
        Map<String, Set<String>> categoriesMethodsMap = new HashMap<>();
        categoriesNode.fields().forEachRemaining(entry -> {
            // send to a set all the methods that match with the given category
            Set<String> methodsByCategory = calls.stream()
            .filter(call -> {
                List<String> methods = new ObjectMapper().convertValue(entry.getValue(), List.class);
                // split method call name to remove the parameters of the comparation
                return methods.contains(call.getMethodName().split("\\[")[0]);
            })
            .map(call -> call.getMethodName().split("\\[")[0])
            .collect(HashSet::new, (set, methodName) -> set.add(methodName), (set, other) -> set.addAll(other));
            categoriesMethodsMap.put(entry.getKey(), methodsByCategory);
        });
        return categoriesMethodsMap;
    }

    private ArrayNode calculateChangeMetricByCategoriesAndInterface(Map<String, Set<String>> categoriesMethodsMap,
            JsonNode categoriesNode, String interfaceSimpleName) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode resultArray = mapper.createArrayNode();
        ArrayNode unionAndIntersectionArray = (ArrayNode) new JsonFile(getClass().getClassLoader().getResourceAsStream("union-intersection.json")).toJsonObject();
        //
        unionAndIntersectionArray.forEach(node ->  {
            boolean hasInterface = false;
            // the compared interfaces in json file that has the respective union and intersection set of methods for the interfaces
            List<String> interfaces = mapper.convertValue(node.get("interfaces"), List.class);
            String otherInterface = "";
            // getting the interfaces used in comparation
            for (String intf : interfaces) {
                if (intf.equals(interfaceSimpleName)) {
                    hasInterface = true;
                } else {
                    otherInterface = intf;
                }
            }
            if (hasInterface) {
                // intersection categories values
                JsonNode intersectionCategories = node.get("intersection");
                // union categories values
                JsonNode unionCategories = node.get("union");
                // condense here all the needed informations for show the factor value, category and interfaces
                Iterator<String> iterator = categoriesNode.fieldNames();
                while (iterator.hasNext()) {
                    String category = iterator.next();
                    // if the categories has no methods, is ignored
                    if (categoriesMethodsMap.get(category).size() == 0)
                        continue;
                    //
                    List<String> intersectionList = mapper.convertValue(intersectionCategories.get(category), List.class);
                    List<String> unionList = mapper.convertValue(unionCategories.get(category), List.class);
                    // verify if the methods in the category are present in intersection set
                    double intersection = categoriesMethodsMap.get(category).stream().filter(intersectionList::contains).count();
                    // get union quantity of methods for the metric calculation
                    double union = categoriesMethodsMap.get(category).stream().filter(unionList::contains).count();
                    /*
                    if the quantity of intersection methods found are equal to the quantity of categorie methods
                    are no dificult to change between the interfaces for this category
                     */
                    double metric = 1 - intersection/union;
                    // mounting result category object
                    ObjectNode resultNode = mapper.createObjectNode();
                    resultNode.set("interfaces", mapper.valueToTree(Arrays.asList(interfaceSimpleName, otherInterface)));
                    resultNode.put("category", category);
                    resultNode.put("metric", metric);
                    //
                    resultArray.add(resultNode);
                }
            }
        });
        return resultArray;
    }
}
