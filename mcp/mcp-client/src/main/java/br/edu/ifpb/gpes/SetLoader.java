package br.edu.ifpb.gpes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ifpb.gpes.jcf.io.JsonFile;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetLoader {

    private static JsonFile json;
    private static CSVPrinter printer;
    private static ArrayNode array;

    public static void main(String[] args) throws IOException {
        json = new JsonFile(SetLoader.class.getClassLoader().getResourceAsStream("sets.json"));
        array = new ObjectMapper().createArrayNode();
        calculateUnionAndIntersection("List", "Map");
        calculateUnionAndIntersection("List", "Set");
        calculateUnionAndIntersection("Set", "Map");
        try {
            new ObjectMapper().writeValue(new File("union-intersection.json"), array);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void calculateUnionAndIntersection(String set1, String set2) {
        List<String> list = Arrays.asList("other", "access", "insertion", "deletion", "search");
        JsonNode jsonSet1 = json.toJsonObject().get(set1);
        JsonNode jsonSet2 = json.toJsonObject().get(set2);
        //
        Set<String> strSet1 = new HashSet<>();
        Set<String> strSet2 = new HashSet<>();
        Set<String> temp = new HashSet<>();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode categoryUnion = mapper.createObjectNode();
        ObjectNode categoryIntersection = mapper.createObjectNode();
        ObjectNode node = mapper.createObjectNode();
        ObjectNode factors = mapper.createObjectNode();

        node.set("interfaces", mapper.convertValue(Arrays.asList(set1, set2), ArrayNode.class));

        list.forEach(c -> {
            jsonSet1.get(c).forEach(e -> strSet1.add(e.asText()));
            jsonSet2.get(c).forEach(e -> strSet2.add(e.asText()));
            temp.addAll(strSet1);
            temp.addAll(strSet2);

            double union = temp.size();

            categoryUnion.set(c, mapper.convertValue(Arrays.asList(temp.toArray()), ArrayNode.class));
            // clear
            temp.clear();
            temp.addAll(strSet1);
            temp.retainAll(strSet2);

            double intersection = temp.size();

            categoryIntersection.set(c, mapper.convertValue(Arrays.asList(temp.toArray()), ArrayNode.class));
            // clear
            strSet1.clear();
            strSet2.clear();
            temp.clear();
            // adapting factor to cost
            double factor = new BigDecimal(1 - (intersection/union)).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
            factors.put(c, factor);
        });
        node.set("union", categoryUnion);
        node.set("intersection", categoryIntersection);
        node.set("factors", factors);
        array.add(node);
    }
}
