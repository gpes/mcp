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
import java.util.*;

public class PerClassMetric {

    public static void main(String[] args) throws IOException {
        InputStream stream = TCCMetricLoader.class.getClassLoader().getResourceAsStream("projects.json");
        JsonFile file = new JsonFile(stream);
        JsonNode projects = file.toJsonObject();
        for (JsonNode p : projects) {
            Map<String, List<Double>> map = new TreeMap<>();
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
                        v = new ArrayList<>();
                    }
                    v.add(Double.valueOf(record.get(5)));
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
                    csvPrinter.printRecord(array[0], array[1], array[2], array[3], harmonicAvarage(v));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            csvPrinter.flush();
        }
    }

    public static double harmonicAvarage(List<Double> values) {
        double denominator = 0;
        for (Double value: values) {
            denominator += 1/value;
        }
        return new BigDecimal(values.size()/denominator).setScale(2, RoundingMode.HALF_DOWN).doubleValue();
    }
}
