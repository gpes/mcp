package ifpb.gpes.graph.io;

import ifpb.gpes.Call;
import ifpb.gpes.ExportManager;
import ifpb.gpes.filter.FilterByMethod;
import ifpb.gpes.filter.FilterClassType;
import ifpb.gpes.graph.*;
import ifpb.gpes.io.FileExportManager;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author juan
 */
public class BrokeExportManager implements ExportManager {

    private final String MATRIX_FILE_NAME = "matrix.csv";
    private final String METRICS_FILE_NAME = "metrics.txt";
    private final String BROKEN_FILE_NAME = "file.txt";
    private String outputDir;

    public BrokeExportManager(String outputDir) {
        this.outputDir = outputDir;
    }

    @Override
    public void export(List<Call> elements) {

        StringBuilder result = new StringBuilder();

        result.append("Todas as calls(").append(elements.size()).append(")\n\n");
        elements.stream().map(Call::callGraph).forEach((call) -> result.append(call).append("\n"));

        List<Predicate<Call>> predicates = Arrays.asList(new FilterClassType("java.util.Map"), new FilterClassType("java.util.Collection"));
        Predicate<Call> compositePredicate = predicates.stream().reduce(w -> true, Predicate::or);

        long count = elements.stream().filter(compositePredicate).count();

        result.append("\n\nCalls pertencentes ao JCF (").append(count).append(")\n\n");
        elements.stream().filter(compositePredicate).forEach((call) -> result.append(call.callGraph()).append("\n"));

        Path path = Paths.get(handleOutputFilePath(outputDir, MATRIX_FILE_NAME));
        Graph graph = new AdapterGraph().apply(elements);

        List<Call> candidates = graph.getCandidates();

        List<Call> verifiedCandidates = candidates.stream().filter(elements::contains).collect(Collectors.toList());

        List<Call> subCandidates = verifiedCandidates.stream().filter(compositePredicate).collect(Collectors.toList());

        result.append("\n\nCalls candidatas(").append(subCandidates.size()).append(")\n\n");
        subCandidates.forEach((call) -> result.append(call.callGraph()).append("\n"));

        subCandidates = subCandidates.stream().filter(new FilterByMethod()).collect(Collectors.toList());

        List<Integer> indices = new ArrayList<>();
        Matrix matrix = graph.toMatrix();
        List<Node> columnsList = Arrays.asList(matrix.getColumns());
        List<Call> brokers = new ArrayList<>();
        for (Call candidate : subCandidates) {
            for (Node node : columnsList) {
                if (nodeFromCall(candidate, node)) {
                    brokers.add(candidate);
                    indices.add(columnsList.indexOf(node));
                }
            }
        }

        result.append("\n\nQuebram o confinamento(").append(brokers.size()).append(")\n\n");
        brokers.forEach((call) -> {
            result.append(call.callGraph()).append("\n");
        });
        write(result.toString());

        new JsonMatrix(matrix).toJson(indices, handleOutputFilePath(outputDir, ""));
        //salvando matrix no arquivo
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (int[] line : matrix.toArray()) {
                for (int column : line) {
                    writer.append(String.valueOf(column));
                    writer.append(",");
                }
                writer.append("\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "problem write csv", ex);
        }
        //salvando metricas no arquivo
        path = Paths.get(handleOutputFilePath(outputDir, METRICS_FILE_NAME));
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (Metric metric : matrix.computeMetric()) {
                writer.append(metric.toString());
                writer.append("\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(FileExportManager.class.getName()).log(Level.SEVERE, "problem write txt", ex);
        }
    }

    protected void write(String text) {
        Path path = Paths.get(handleOutputFilePath(outputDir, BROKEN_FILE_NAME));
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(text);
        } catch (IOException ex) {
            Logger.getLogger(FileExportManager.class.getName()).log(Level.SEVERE, "problem write file", ex);
        }
    }

    public boolean nodeFromCall(Call call, Node node) {
        if (!call.getMethodName().equals(node.getMethodName())) {
            return false;
        }
        if (!call.getClassType().equals(node.getClassName())) {
            return false;
        }
        if (!call.getReturnType().equals(node.getReturnType())) {
            return false;
        }
        if (!call.getInvokedBy().equals(node.getInvokedBy())) {
            return false;
        }
        return true;
    }

    public String handleOutputFilePath(String dir, String filename) {
        return dir.endsWith("/") ? dir + filename: dir + '/' + filename;
    }
}
