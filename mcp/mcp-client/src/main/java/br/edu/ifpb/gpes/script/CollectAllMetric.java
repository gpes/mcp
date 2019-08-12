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
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CollectAllMetric {

    private static JsonFile unionIntersectionFile;

    public static void main(String[] args) throws IOException {
        InputStream stream = TCCMetricLoader.class.getClassLoader().getResourceAsStream("projects.json");
        JsonFile file = new JsonFile(stream);
        unionIntersectionFile = new JsonFile(TCCMetricLoader.class.getClassLoader().getResourceAsStream("union-intersection.json"));
        JsonNode projects = file.toJsonObject();
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("../tcc-outputs/cost/AIO-metrics.csv"));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("project", "class", "from", "to", "category", "metric")
                .withDelimiter(';'));
        for (JsonNode p : projects) {
            Map<String, Map<String, Double>> map = new TreeMap<>();
            Reader reader = Files.newBufferedReader(Paths.get("../tcc-outputs/cost/" + p.get("path").asText() + "-" + "metrics.csv"));
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(';')
                    .withIgnoreHeaderCase());
            parser.forEach(record -> {
                try {
                    csvPrinter.printRecord(record.get(0), record.get(1), record.get(2), record.get(3), record.get(4), record.get(5));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        csvPrinter.flush();
    }
}
