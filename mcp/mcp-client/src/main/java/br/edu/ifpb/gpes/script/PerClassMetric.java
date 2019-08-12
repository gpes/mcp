package br.edu.ifpb.gpes.script;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ifpb.gpes.jcf.io.JsonFile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class PerClassMetric {

    private static JsonFile unionIntersectionFile;

    public static void main(String[] args) throws IOException {
        InputStream stream = TCCMetricLoader.class.getClassLoader().getResourceAsStream("projects.json");
        JsonFile file = new JsonFile(stream);
        unionIntersectionFile = new JsonFile(TCCMetricLoader.class.getClassLoader().getResourceAsStream("union-intersection.json"));
        JsonNode projects = file.toJsonObject();
        for (JsonNode p : projects) {
            Map<String, Map<String, Double>> map = new TreeMap<>();
            Reader reader = Files.newBufferedReader(Paths.get("../tcc-outputs/cost/" + p.get("path").asText() + "-" + "metrics.csv"));
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(';')
                    .withIgnoreHeaderCase());
            parser.forEach(record -> {
                String key = new StringBuffer()
                        .append(record.get(0)).append(':')
                        .append(record.get(1)).append(':')
                        .append(record.get(2)).append(':')
                        .append(record.get(3)).append(':')
                        .toString();
                map.compute(key, (k, v) -> {
                    if (v == null) {
                        v = new HashMap<>();
                    }
                    v.put(record.get(4), Double.valueOf(record.get(5)));
                    return v;
                });

            });
            BufferedWriter writer = Files.newBufferedWriter(Paths.get("../tcc-outputs/cost/per-class/" + p.get("path").asText() + "-per-class-metrics.csv"));
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader("project", "class", "from", "to", "metric")
                    .withDelimiter(';'));
            map.forEach((k, v) -> {
                String[] array = k.split(":");
                try {
                    csvPrinter.printRecord(array[0], array[1], array[2], array[3], calculateCategoryAvarage( array[2], array[3], v));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            csvPrinter.flush();
        }
    }

    public static double calculateCategoryAvarage(String from, String to, Map<String, Double> categories) {
        List<Double> factors = new ArrayList<>();
        for(JsonNode node : unionIntersectionFile.toJsonObject()) {
            List<String> interfaces = new ObjectMapper().convertValue(node.get("interfaces"), List.class);
            if (interfaces.contains(from) && interfaces.contains(to)) {
                categories.keySet().stream()
                    .peek(category -> {
                        factors.add(node.get("factors").get(category).asDouble());
                    })
                    .forEach(category -> {
                        categories.compute(category, (c, v) -> {
                            // precaucao
                            if (v != null) {
                                return v * node.get("factors").get(category).asDouble();
                            }
                            return v;
                        });
                    });
                double values = categories.values().stream().collect(Collectors.summarizingDouble(Double::doubleValue)).getSum();
                double aux_weights = factors.stream().collect(Collectors.summarizingDouble(Double::doubleValue)).getSum();
                double weights = aux_weights == 0.0d ? 1 : aux_weights;
                return new BigDecimal(values/weights).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
            }
        };
        return 0.0d;
    }
}
