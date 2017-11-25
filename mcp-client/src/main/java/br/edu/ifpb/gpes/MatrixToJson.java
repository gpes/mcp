package br.edu.ifpb.gpes;

import br.edu.ifpb.gpes.old.MatrixEx;
import ifpb.gpes.graph.Matrix;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 25/11/2017, 09:56:38
 */
public class MatrixToJson {

    private Matrix matrix;

    public MatrixToJson(Matrix matrix) {
        this.matrix = matrix;
    }

    public void toJson() {
//        System.out.println(matrix.valuesToString());
        String nodes = nodesToJson(matrix);
        String edges = edgesToJson(matrix);
        escreverNoArquivo(nodes, edges, matrix.namesColumns());
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
        return array;
    }

    private String nodesToJson(Matrix matrix) {
        String[] namesColumns = matrix.namesColumns();
        String collect = IntStream.range(0, namesColumns.length)
                .filter(this.matrix::conectado)
                .mapToObj(i
                        -> String.format("{\"id\":\"%d\", \"label\":\"%s\"}",
                        //                        i, namesColumns[i]))
                        i, String.valueOf(i)))
                .collect(Collectors.joining(", ", "[", "]"));
        return collect;
    }

    private void escreverNoArquivo(String nodes, String edges,
            String[] namesColumns) {
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
                + " var ns = document.getElementById('nodes');"
                + " ns.innerText = '" + colunas(namesColumns) + "';"
                + "}";
        salvarArquivo(file);

    }

    private void salvarArquivo(String texto) {
        Path get = Paths.get("./src/main/java/br/edu/ifpb/gpes/script.js");
        try (BufferedWriter writer = Files.newBufferedWriter(get)) {
            writer.write(texto);
        } catch (IOException ex) {
            Logger.getLogger(MatrixEx.class.getName()).log(Level.SEVERE, "problem write file", ex);
        }
    }

    private String colunas(String[] namesColumns) {
        String collect = IntStream.range(0, namesColumns.length)
                .mapToObj(x -> String.format("%d - %s", x, namesColumns[x]))
                .collect(Collectors.joining(" "));
        return collect;
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

        public String toJson() {
            return String.format("{\"from\":\"%s\", "
                    + "\"to\":\"%s\", \"arrows\":\"to\", \"label\":\"%s\"}",
                    from, to, label);

        }

    }
}
