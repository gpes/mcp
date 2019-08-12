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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PerCategoryMetric {

    public static void main(String[] args) throws IOException {
        Map<String, List<Double>> map = new TreeMap<>();
        InputStream stream = TCCMetricLoader.class.getClassLoader().getResourceAsStream("projects.json");
        JsonFile file = new JsonFile(stream);
        JsonNode projects = file.toJsonObject();
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("../tcc-outputs/cost/per-project/AIO-per-category-metrics.csv"));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("category", "from", "to", "metric")
                .withDelimiter(';'));
        for (JsonNode p : projects) {
            Reader reader = Files.newBufferedReader(Paths.get("../tcc-outputs/cost/" + p.get("path").asText() + "-metrics.csv"));
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(';')
                    .withIgnoreHeaderCase());
            parser.forEach(record -> {
                String key = new StringBuffer()
                        .append(record.get(4)).append(':')
                        .append(record.get(2)).append(':')
                        .append(record.get(3)).append(':')
                        .toString();
                map.compute(key, (k, v) -> {
                    if (v == null) {
                        v = new ArrayList<>();
                    }
                    v.add(Double.valueOf(record.get(5)));
                    return v;
                });
            });
        }
        map.forEach((k, v) -> {
            String[] array = k.split(":");
            try {
                double avarage = v.stream().mapToDouble(d -> d).average().orElse(0.0d);
                csvPrinter.printRecord(array[0], array[1], array[2], new BigDecimal(avarage).setScale(2, RoundingMode.HALF_DOWN).doubleValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        csvPrinter.flush();
    }
}
