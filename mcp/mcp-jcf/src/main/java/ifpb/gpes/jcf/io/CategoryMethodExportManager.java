package ifpb.gpes.jcf.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ifpb.gpes.Call;
import ifpb.gpes.ExportManager;
import ifpb.gpes.filter.AssignVerifier;
import ifpb.gpes.filter.FilterClassType;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

public class CategoryMethodExportManager extends ExportManager {

    private final String CATEGORY_FILE = "category-method.json";
    private final String CATEGORIES_JSON = "categories.json";

    public CategoryMethodExportManager(String outputDir) {
        super(outputDir);
    }

    @Override
    public void export(List<Call> elements) {
        List<String> interfaces = Arrays.asList("java.util.List", "java.util.Map", "java.util.Set");
        AssignVerifier verifier = new AssignVerifier();

        File file = Paths.get(handleOutputFilePath(outputDir, "")).toFile();

        if (!file.exists()) {
            file.mkdirs();
        }

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CATEGORIES_JSON);

        JsonFile jsonFile = new JsonFile(inputStream);

        JsonNode categories = jsonFile.toJsonObject();

        Map<String, ObjectNode> resultMap = new HashMap<>();

        Predicate<Call> predicate = new FilterClassType("java.util.Map").or(new FilterClassType("java.util.Collection"));

        ObjectMapper mapper = new ObjectMapper();

        elements.stream().filter(predicate).forEach(e -> {
            // get the assignable Interface
            Optional<String> opt = interfaces.stream().filter(i -> {
                verifier.setBaseClass(i);
                return verifier.isAssignable(e.getClassType());
            }).findAny();
            //
            if (!opt.isPresent())
                return;
            //
            Iterator<String> fieldNames = categories.fieldNames();
            fieldNames.forEachRemaining(field -> {
                // parse the method name string
                String method = e.getMethodName().split("\\[")[0];
                //
                ArrayNode category = (ArrayNode) categories.get(field);
                category.forEach(m -> {
                    if (m.asText().equals(method)) {
                        // compute here
                        resultMap.compute(opt.get(), (intf, node) -> {
                            node = node == null ? mapper.createObjectNode() : node;
                            ObjectNode methodsNode;
                            if (!node.hasNonNull(field)) {
                                methodsNode = mapper.createObjectNode();
                            } else {
                                methodsNode = (ObjectNode) node.get(field);
                            }
                            if (methodsNode.hasNonNull(method))
                                methodsNode.put(method, methodsNode.get(method).asInt() + 1);
                            else
                                methodsNode.put(method, 1);
                            node.set(field, methodsNode);
                            return node;
                        });
                    }
                });
            });
        });
        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resultMap);
            write(json, Paths.get(handleOutputFilePath(outputDir, CATEGORY_FILE)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
