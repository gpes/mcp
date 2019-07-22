package ifpb.gpes.jcf.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import ifpb.gpes.Call;
import ifpb.gpes.ExportManager;
import ifpb.gpes.filter.FilterClassType;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ChangeDifficultExportManager extends ExportManager {

    public ChangeDifficultExportManager(String outputDir) {
        super(outputDir);
    }

    @Override
    public void export(List<Call> elements) {
        Predicate<Call> predicate = new FilterClassType("java.util.Map").or(new FilterClassType("java.util.Collection"));
        List<Call> calls = elements.stream().filter(predicate).collect(Collectors.toList());
        calculateFactors(calls, "java.util.List");
    }

    private void calculateFactors(List<Call> calls, String interfaceFullName) {
        ObjectMapper mapper = new ObjectMapper();
        String[] splitedInterfaceName = interfaceFullName.split("\\.");
        String interfaceSimpleName = splitedInterfaceName[splitedInterfaceName.length-1];
        JsonNode categoriesNode = new JsonFile(getClass().getClassLoader().getResourceAsStream("categories.json")).toJsonObject();
        ArrayNode unionAndIntersectionArray = (ArrayNode) new JsonFile(getClass().getClassLoader().getResourceAsStream("union-intersection.json")).toJsonObject();
        Map<String, Set<String>> categoriesMethodsMap = new HashMap<>();
        List<Call> filteredCalls = calls.stream().filter(new FilterClassType(interfaceFullName)).collect(Collectors.toList());
        //
        categoriesNode.fields().forEachRemaining(entry -> {
            Set<String> methodsByCategory = filteredCalls.stream().filter(call -> {
                List<String> methods = mapper.convertValue(entry.getValue(), List.class);
                Iterator<JsonNode> iterator = entry.getValue().iterator();
                //
                return methods.contains(call.getMethodName().split("\\[")[0]);
            }).map(call -> call.getMethodName().split("\\[")[0]).collect(HashSet::new, (set, methodName) -> {
                set.add(methodName);
            }, (set, other) -> set.addAll(other));
            categoriesMethodsMap.put(entry.getKey(), methodsByCategory);
        });
        //
        unionAndIntersectionArray.forEach(node ->  {
            boolean hasInterface = false;
            List<String> interfaces = mapper.convertValue(node.get("interfaces"), List.class);
            String otherInterface = "";
            //
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
                    List<String> intersectionList = mapper.convertValue(intersectionCategories.get(category), List.class);
                    long intersection = categoriesMethodsMap.get(category).stream().filter(intersectionList::contains).count();
                    long union = unionCategories.get(category).size();
                    System.out.println("interfaces -> " + interfaceSimpleName + " to " + otherInterface);
                    System.out.println("category -> " + category);
                    System.out.println("metric -> " + calculateMetric(intersection, union));
                    System.out.println("-------------");
                }
            }
        });
    }

    public double calculateMetric(long intersection, long union) {
        return 1 / (1 + (intersection/union));
    }
}
