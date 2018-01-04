package br.edu.ifpb.gpes;

import br.edu.ifpb.gpes.old.MatrixEx;
import ifpb.gpes.graph.Matrix;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.function.IntFunction;
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

    public void toJson(List<Integer> indices) {
//        System.out.println(matrix.valuesToString());
        String nodes = nodesToJson(matrix, indices);
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

    private String nodesToJson(Matrix matrix, List<Integer> indices) {
        String[] namesColumns = matrix.namesColumns();
        String collect = IntStream.range(0, namesColumns.length)
                .filter(this.matrix::conectado)
                .mapToObj(new IntFunction<String>() {
                    @Override
                    public String apply(int i) {
                        if (indices.contains(i)) {
                            return String.format("{\"id\":\"%d\", \"label\":\"%s\","
                                    + "\"color\":{\"border\": \"black\", \"background\": \"red\"}}",
                                    //i, namesColumns[i]))
                                    i, String.valueOf(i));
                        }
                        return String.format("{\"id\":\"%d\", \"label\":\"%s\"}",
                                //i, namesColumns[i]))
                                i, String.valueOf(i));
                    }
                })
                .collect(Collectors.joining(", ", "[", "]"));
        return collect;
    }

    private void escreverNoArquivo(String nodes, String edges,
            String[] namesColumns) {
        Path script = Paths.get("./src/main/java/br/edu/ifpb/gpes/script.js");
        Path elements = Paths.get("./src/main/java/br/edu/ifpb/gpes/elements.json");
        String elementsFile = "{\n"
                + "	\"nodes\":" + nodes + ",\n"
                + "	\"edges\":" + edges + "\n"
                + "}";

        salvarArquivo(elementsFile, elements);

//        String file = "var imported = document.createElement('script');"
//                + "imported.src = 'elements.js';"
//                + "document.head.appendChild(imported);"
//                + "function fillGraph() { "
//                + " var data = {"
//                + "     nodes: JSON.parse(nodes),"
//                + "     edges: JSON.parse(arr)"
//                + " };"
//                + " var options = {};"
//                + " var container = document.getElementById('graph');"
//                + " var network = new vis.Network(container, data, options);"
//                + " var ns = document.getElementById('nodes');"
//                + " ns.innerHTML = `" + colunasFormatadasHtml(namesColumns) + "`;"
//                + "}";
//        salvarArquivo(file, script);
        String texto = "\n ns.innerHTML = `" + colunasFormatadasHtml(namesColumns) + "`;";
        atualizarScript(texto, script);
    }

    private void salvarArquivo(String texto, Path path) {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(texto);
        } catch (IOException ex) {
            Logger.getLogger(MatrixEx.class.getName()).log(Level.SEVERE, "problem write file", ex);
        }
    }

    private void atualizarScript(String texto, Path path) {
        try {
            Files.write(path, texto.getBytes(), StandardOpenOption.APPEND);
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

    private String colunasFormatadasHtml(String[] namesColumns) {
        return IntStream.range(0, namesColumns.length)
                .mapToObj(x -> String.format("<p class=\"col-md-4\"><span class=\"badge\">%d</span> %s</p>", x, namesColumns[x]))
                .collect(Collectors.joining("\n"));
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
