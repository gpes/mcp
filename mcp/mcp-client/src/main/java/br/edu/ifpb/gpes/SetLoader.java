package br.edu.ifpb.gpes;

import com.fasterxml.jackson.databind.JsonNode;
import ifpb.gpes.jcf.io.JsonFile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetLoader {

    private static JsonFile json;
    private static CSVPrinter printer;


    public static void main(String[] args) throws IOException {
        json = new JsonFile(SetLoader.class.getClassLoader().getResourceAsStream("sets.json"));
        BufferedWriter writer = Files.newBufferedWriter(
                Paths.get("default_factors.csv"),
                StandardOpenOption.APPEND,
                StandardOpenOption.CREATE);
        printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withDelimiter(';').withHeader("set_1", "set_2", "category", "factor"));
        calcularFator("List", "Map");
        calcularFator("List", "Set");
        calcularFator("Set", "Map");
        printer.flush();
    }

    public static void calcularFator(String set1, String set2) {
//        System.out.println(set1 + " - " + set2);
        List<String> list = Arrays.asList("other", "access", "insertion", "deletion", "search");
        JsonNode jsonSet1 = json.toJsonObject().get(set1);
        JsonNode jsonSet2 = json.toJsonObject().get(set2);
        //
        Set<String> strSet1 = new HashSet<>();
        Set<String> strSet2 = new HashSet<>();
        Set<String> temp = new HashSet<>();
        list.forEach(c -> {
//            System.out.println("category -> " + c);
            jsonSet1.get(c).forEach(e -> strSet1.add(e.asText()));
            jsonSet2.get(c).forEach(e -> strSet2.add(e.asText()));
            temp.addAll((HashSet<String>) strSet1);
            temp.addAll(strSet2);
            // union
//            System.out.println("union -> " + temp);
            int unionSize = temp.size();
            // intersecction
            // clear
            temp.clear();
            temp.addAll(strSet1);
            temp.retainAll(strSet2);
//            System.out.println("intersection -> " + temp);
            int intersectionSize = temp.size();
            // clear
            strSet1.clear();
            strSet2.clear();
            temp.clear();
            BigDecimal factor = new BigDecimal((double) intersectionSize / unionSize).setScale(2, RoundingMode.HALF_UP);
            try {
                printer.printRecord(set1, set2, c, factor.doubleValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
