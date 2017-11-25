package br.edu.ifpb.gpes.old;

import ifpb.gpes.graph.Matrix;
import ifpb.gpes.io.FileExportManager;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 22/11/2017, 08:38:26
 */
public class MatrixEx {
     
    
    public static void main(String[] args) {
        int[][] matrixValues = {
            {0, 2, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 2},
            {0, 1, 0, 3, 0, 0},
            {0, 0, 0, 0, 1, 2},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0}
        };

//        salvarArquivo();
//                build(matrixValues);
//        print(matrixValues);
    }

    private static void print(int[][] matrix) {
        StringBuilder builder = new StringBuilder();
        for (int[] row : matrix) {
            for (int j = 0; j < matrix.length; j++) {
                builder.append(row[j]).append(" ");
            }
            builder.append("\n");
        }

        System.out.println(builder);

        StringBuilder collect = Arrays.stream(matrix)
                .flatMapToInt(Arrays::stream)
                .collect(StringBuilder::new, (t, value) -> {
                    t.append(value).append(" ");
                }, new BiConsumer<StringBuilder, StringBuilder>() {
                    @Override
                    public void accept(StringBuilder t, StringBuilder u) {
                        t.append(u).append("\n");
                    }
                }); //                .map(a -> Arrays.toString(a))
        //                .collect(Collectors.joining("\n"));
        //                .collect(Integer::new,
        //                        StringBuilder::append, StringBuilder::append);
        System.out.println(collect);

//        String commaSeparatedNumbers = Arrays.stream(matrix)
//                .mapToObj(Integer::toString)
//                .collect(Collectors.joining(", "));
    }

    private static void build(int[][] matrixValues) {
        Matrix matrix = new Matrix(matrixValues);
        String[] names = {"A", "B", "C", "D", "E", "F"};
        IntStream.range(0, names.length)
                .forEach(i -> matrix.updateNameColumn((i), names[i]));

        System.out.println(matrix.valuesToString());

        String nodes = nodesToJson(matrix);
        String edges = edgesToJson(matrix);
        escreverNoArquivo(nodes, edges);
    }

    private static String edgesToJson(Matrix matrix) {
        int[][] matrixs = matrix.toArray();

        String array = IntStream.range(0, matrixs.length)
                .mapToObj(x -> IntStream.range(0, matrixs.length)
                .filter(f -> matrixs[x][f] != 0)
                .mapToObj(y -> new EdgeVis(x, y, matrixs[x][y]))
                .collect(Collectors.toList()))
                .flatMap(v -> v.stream())
                .map(EdgeVis::toJson)
                .collect(Collectors.joining(", ", "[", "]"));

//        List<EdgeVis> collect = IntStream.range(0, matrix.length)
//                .mapToObj(x -> IntStream.range(0, matrix.length)
//                .filter(f -> matrix[x][f] != 0)
//                .mapToObj(y -> new EdgeVis(x, y, matrix[x][y]))
//                .collect(Collectors.toList()))
//                .flatMap(v -> v.stream())
//                .collect(Collectors.toList());
//
//        String array = collect
//                .stream()
//                .map(EdgeVis::toJson)
//                .collect(Collectors.joining(", ", "[", "]"));
        System.out.println(array);
//        collect.forEach(e -> System.out.println(e.toJson()));
        return array;
    }

    //{id: 1, label: 'm1()'},
    private static String nodesToJson(Matrix matrix) {
        String[] namesColumns = matrix.namesColumns();
        String collect = IntStream.range(0, namesColumns.length)
                .mapToObj(i
                        -> String.format("{\"id\":\"%d\", \"label\":\"%s\"}",
                        i, namesColumns[i]))
                .collect(Collectors.joining(", ", "[", "]"));
        System.out.println(collect);
        return collect;
    }

    private static void escreverNoArquivo(String nodes, String edges) {
        String file = "function fillGraph() { "
                + " var nodes = '" + nodes + "';"
                + " var arr = '" + edges + "';"
                + " var data = {"
                + "     nodes: JSON.parse(nodes),"
                + "     edges: JSON.parse(arr)"
                + " };"
                + " var options = {};"
                + " var container = document.getElementById('graph');"
                + " var network = new vis.Network(container, data, options);"
                + "}";
        System.out.println("file = " + file);
        salvarArquivo(file);

    }

    private static void salvarArquivo(String texto) {

        Path get = Paths.get("/Users/job/Documents/dev/gpes/mcp/mcp-client/src/main/java/br/edu/ifpb/gpes/script.js");
        try (BufferedWriter writer = Files.newBufferedWriter(get)) {
            writer.write(texto);
        } catch (IOException ex) {
            Logger.getLogger(MatrixEx.class.getName()).log(Level.SEVERE, "problem write file", ex);
        }
    }

    private static class EdgeVis {

        private final String from;
        private final String to;
        private final String label;
        private final String arrows = "to";

        public EdgeVis(String from, String to, String label) {
            this.from = from;
            this.to = to;
            this.label = label;
        }

        public EdgeVis() {
            this("", "", "");
        }

        private EdgeVis(int from, int to, int label) {
            this(String.valueOf(from),
                    String.valueOf(to),
                    String.valueOf(label));
        }

        @Override
        public String toString() {
            return "EdgeVis{" + "from=" + from + ", to=" + to + ", label=" + label + ", arrows=" + arrows + '}';
        }

        //{ "from":"2", "to":"1", "arrows":"to", "label":"3"}
        public String toJson() {
            return String.format("{\"from\":\"%s\", "
                    + "\"to\":\"%s\", \"arrows\":\"to\", \"label\":\"%s\"}",
                    from, to, label);

        }

    }
}
