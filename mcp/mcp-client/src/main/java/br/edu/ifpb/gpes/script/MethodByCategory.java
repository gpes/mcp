package br.edu.ifpb.gpes.script;

import com.fasterxml.jackson.databind.JsonNode;
import ifpb.gpes.jcf.io.JsonFile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;

public class MethodByCategory {

    public static void main(String[] args) throws IOException {
        Map<String, Double> map = new TreeMap<>();
        //
        InputStream stream = TCCMetricLoader.class.getClassLoader().getResourceAsStream("projects.json");
        JsonFile file = new JsonFile(stream);
        JsonNode projects = file.toJsonObject();
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("../tcc-outputs/methods-quant-by-set.csv"));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("project", "interface", "category", "value").withDelimiter(';'));
        for (JsonNode p : projects) {
            Reader reader = Files.newBufferedReader(Paths.get("./" + p.get("path").asText() + ".csv"));
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(';')
                    .withIgnoreHeaderCase());
            parser.forEach(record -> {
                String key = p.get("path").asText() + ":" + record.get(0) + ":" + record.get(1);
                map.compute(key, (k, v) -> {
                    if (v == null) {
                        v = 0d;
                    }
                    v += Double.valueOf(record.get(3));
                    return v;
                });
            });
        }
        map.forEach((k, v) -> {
            String[] array = k.split(":");
            try {
                csvPrinter.printRecord(array[0], array[1], array[2], v);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        csvPrinter.flush();
    }
}
