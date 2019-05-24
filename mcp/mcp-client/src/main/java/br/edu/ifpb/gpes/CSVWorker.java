package br.edu.ifpb.gpes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CSVWorker {

    public static void main(String[] args) {
        String extension = ".csv";
        String dir = "/home/shotaro/myProjects/pp-mcp/tcc-outputs/statistic/";
        List<String> projects = Arrays.asList("collections", "freecs", "jedit", "jext", "jfreechart", "jgrapht", "marauroa", "pmd", "quartz", "quickserver");
        List<String> categories = Arrays.asList("other", "search", "insertion", "deletion", "access");
        List<String> classes = Arrays.asList("List", "Map", "Set");
        //
        Map<String, int[]> map = new HashMap<>();
        //
        projects.forEach(p -> {
            BufferedReader reader = null;
            String line = "";
            int index = 0;
            try {
                reader = new BufferedReader(new FileReader(dir + p + extension));
                while ((line = reader.readLine()) != null) {
                    if (index == 0) {
                        index++;
                        continue;
                    }
                    String[] values = line.split(";");
                    if (!map.containsKey(values[0])) {
                        String[] filteredStrValues = Arrays.copyOfRange(values, 1, values.length);
                        int[] filteredValues = Arrays.asList(filteredStrValues).stream().mapToInt(Integer::valueOf).toArray();
                        map.put(values[0], filteredValues);
                    } else {
                        int[] counts = map.get(values[0]);
                        for (int i = 0; i < counts.length; i++) {
                            counts[i] += Integer.valueOf(values[i+1]);
                        }
                    }
                }
                index = 0;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        map.forEach((k, arr) -> {
            System.out.println(k + ":");
            Arrays.stream(arr).forEach(v -> System.out.print(v + " "));
            System.out.println("\n");
        });
    }
}
