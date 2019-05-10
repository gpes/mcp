package ifpb.gpes.jcf.io;

import ifpb.gpes.Call;
import ifpb.gpes.ExportManager;
import ifpb.gpes.filter.AssignVerifier;
import ifpb.gpes.filter.FilterClassType;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Predicate;

public class CategoryExportManager implements ExportManager {

    private final String CATEGORY_FILE = "category-filtered.json";
    private final String CATEGORIES_JSON = "categories.json";
    private String outputDir;

    public CategoryExportManager(String outputDir) {
        this.outputDir = outputDir;
    }

    @Override
    public void export(List<Call> elements) {
        List<String> interfaces = Arrays.asList("java.util.List", "java.util.Map", "java.util.Set");
        AssignVerifier verifier = new AssignVerifier();

        File file = Paths.get(handleOutputFilePath(outputDir, "")).toFile();
        if(!file.exists()) {
            file.mkdirs();
        }

        Predicate<Call> predicate = new FilterClassType("java.util.Map").or(new FilterClassType("java.util.Collection"));

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CATEGORIES_JSON);

        JsonFile jsonFile = new JsonFile(inputStream);

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        Map<String, JsonObject> map = new WeakHashMap();

        elements.stream().filter(predicate).forEach(e -> {
            // get the assignable Interface
            String inf = interfaces.stream().filter(i -> {
                verifier.setBaseClass(i);
                return verifier.isAssignable(e.getClassType());
            }).findAny().get();
            JsonObject categoriesObject = jsonFile.toJsonObject();
            categoriesObject.forEach((k, v) -> {
                if(v.asJsonArray().contains(e.getMethodName().split("\\[")[0])) {
                    //compute here
                }
            });
            //
        });
    }
}
