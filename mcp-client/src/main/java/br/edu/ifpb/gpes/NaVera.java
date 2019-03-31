package br.edu.ifpb.gpes;

import ifpb.gpes.Call;
import ifpb.gpes.ExportManager;
import ifpb.gpes.Project;
import ifpb.gpes.filter.FilterByMethod;
import ifpb.gpes.filter.FilterClassType;
import ifpb.gpes.graph.AdapterGraph;
import ifpb.gpes.graph.Graph;
import ifpb.gpes.graph.Matrix;
import ifpb.gpes.graph.Node;
import ifpb.gpes.io.FileExportManager;
import ifpb.gpes.jdt.DefaultVisitor;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 29/03/2019, 08:06:36
 */
public class NaVera {

    public static void main(String[] args) {
        List<Call> elements = new ArrayList<>();
        ASTVisitor visitor = new DefaultVisitor(elements);
        Project project = Project
            .root("/Users/job/Desktop/collections-3.2.1/")
            .path("src/java/")
            .sources("src/java/")
            .filter(".java");
        Default parser = Default.from(project.sources());
        project.files().forEach(p -> {
            parser.updateUnitName(p);
            parser.acceptVisitor(visitor);
        });

        ExportManager strategy = new ExportVoidNavera(true,true);
        strategy.export(elements);
//        The forName method that resides inside the Class class.
//The findSystemClass method that resides inside the ClassLoader class.
//The loadClass method that resides inside the ClassLoader class.

    }
}

class Default {

    private final ASTParser parser = ASTParser.newParser(AST.JLS8);
    private final String[] sources;
    private Hashtable options = JavaCore.getOptions();
    private final String[] classpath = {
        System.getProperty("java.home") + "/lib/rt.jar",
        //        "/Users/job/Desktop/jext-5.0/lib/*.jar"
        "/Users/job/Desktop/collections-3.2.1/bin"
    };

    public static Default createParse(String[] sources) {

        if (sources == null) {
            throw new IllegalArgumentException("sources is null");
        }

        return new Default(sources);
    }

    public static Default from(String... sources) {

        if (sources == null) {
            throw new IllegalArgumentException("path is null");
        }

        return new Default(sources);
    }

    private Default(String[] sources) {
        this.sources = sources;
        this.parser.setKind(ASTParser.K_COMPILATION_UNIT);
        options.put(JavaCore.COMPILER_COMPLIANCE,JavaCore.VERSION_1_8);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM,JavaCore.VERSION_1_8);
        options.put(JavaCore.COMPILER_SOURCE,JavaCore.VERSION_1_8);
//        this.parser.setCompilerOptions(JavaCore.getOptions());
    }

    public void updateUnitName(Path fileJava) {
        try {
            byte[] readAllBytes = Files.readAllBytes(fileJava);
            String str = new String(readAllBytes);
            this.parser.setBindingsRecovery(true);
            this.parser.setResolveBindings(true);
            this.parser.setUnitName(fileJava.getFileName().toString());
            this.parser.setEnvironment(classpath,sources,new String[]{"UTF-8"},true);
            this.parser.setSource(str.toCharArray());
        } catch (IOException ex) {
            Logger.getLogger(Default.class.getName()).log(Level.SEVERE,null,ex);
        }
    }

    public void acceptVisitor(ASTVisitor visitor) {
        this.parser.setCompilerOptions(options);
        ASTNode createAST = parser.createAST(null);
        if (createAST.getAST().hasBindingsRecovery()) {
            Logger.getLogger(Default.class.getName()).log(Level.INFO,"Binding activated.");
        }
        createAST.accept(visitor);
    }
}

class ExportVoidNavera implements ExportManager {

//        private final String MATRIX_FILE_PATH = "../matrix.csv";
//        private final String METRICS_FILE_PATH = "../metrics";
//        private final ExportManager origin = FilteredFileExportManager.name("file");
    private final boolean calls;
    private final boolean matriz;

    public ExportVoidNavera(boolean CallsEnabled,boolean MatrixEnabled) {
        this.calls = CallsEnabled;
        this.matriz = MatrixEnabled;
    }

    @Override
    public void export(List<Call> elements) {

//            if (calls) {
//                origin.export(elements);
//            }
//            if (matriz) {
        StringBuilder result = new StringBuilder();
        result.append("Todas as calls(").append(elements.size()).append(")\n\n");
        elements.stream().map(Call::callGraph).forEach((call) -> result.append(call).append("\n"));

        List<Predicate<Call>> predicatesClasses = Arrays
            .asList(new FilterClassType("java.util.Map"),new FilterClassType("java.util.Collection"));
        Predicate<Call> compositeClasses
                        = predicatesClasses.stream().reduce(w -> true,Predicate::or);

        long jcfCount = elements.stream().filter(new FilterClassType("java.util.Collection").or(new FilterClassType("java.util.Map"))).count();
        result.append("\n\nCalls pertencentes ao JCF (").append(jcfCount).append(")\n\n");
        elements.stream().filter(new FilterClassType("java.util.Collection").or(new FilterClassType("java.util.Map"))).forEach((call) -> result.append(call.callGraph()).append("\n"));

//                StringBuilder broken = new StringBuilder();
//                broken.append("\n\nQuebram o confinamento\n\n");
//                Path path = Paths.get(MATRIX_FILE_PATH);
        Graph graph = new AdapterGraph().apply(elements);
        List<Call> candidates = graph.getCandidates();

        List<Call> subCandidates = new ArrayList<>();
//                candidates.stream().filter(compositeClasses).collect(Collectors.toList());
//                int trueCandidates = 0;
        for (Call candidate : candidates) {
            if (elements.contains(candidate)) {
//                        trueCandidates++;
//                        result.append(subCandidate.callGraph()).append("\n");
                subCandidates.add(candidate);
            }
        }
        subCandidates = subCandidates.stream().filter(new FilterClassType("java.util.Collection").or(new FilterClassType("java.util.Map")))
            .collect(Collectors.toList());
        result.append("\n\nCalls candidatas(").append(subCandidates.size()).append(")\n\n");
        subCandidates.forEach((call) -> result.append(call.callGraph()).append("\n"));

        List<Predicate<Call>> predicates = Arrays
            .asList(
                new FilterByMethod(),
                new FilterClassType("java.util.Collection")
            );
        Predicate<Call> compositePredicate
                        = predicates.stream().reduce(w -> true,Predicate::and);
//              elements = elements.stream().filter(compositePredicate).collect(Collectors.toList());
        subCandidates = subCandidates.stream().filter(
            new FilterByMethod()
        ).collect(Collectors.toList());
        List<Integer> indices = new ArrayList<>();
        Matrix matrix = graph.toMatrix();
        List<Node> columnsList = Arrays.asList(matrix.getColumns());
        List<Call> brokers = new ArrayList<>();
//                for (Call candidate : candidates) {
        for (Call candidate : subCandidates) {
//                    if (elements.contains(candidate)) {
            for (Node node : columnsList) {
                if (nodeFromCall(candidate,node)) {
//                            brokers++;
//                            broken.append(candidate.callGraph()).append("\n");
                    brokers.add(candidate);
                    indices.add(columnsList.indexOf(node));
                }
            }
//                    }
        }

        result.append("\n\nQuebram o confinamento(").append(brokers.size()).append(")\n\n");
        brokers.forEach((call) -> {
            result.append(call.callGraph()).append("\n");
        });

        String collect = candidates
            .stream()
            .map(c -> c.toString())
            .collect(Collectors.joining(";"));
        result.append("candidadots: ").append(collect);
        
        write(result.toString());
        

//                System.out.println(Arrays.toString(matrix.namesColumns()));
//                System.out.println(matrix.valuesToString());
//                matrix.computeMetric().forEach(System.out::println);
//                new MatrixToJson(matrix).toJson(indices);
        //salvando matrix no arquivo
//                try (BufferedWriter writer = Files.newBufferedWriter(path)) {
//                    for (int[] line : matrix.toArray()) {
//                        for (int column : line) {
//                            writer.append(String.valueOf(column));
//                            writer.append(",");
//                        }
//                        writer.append("\n");
//                    }
//                } catch (IOException ex) {
//                    Logger.getLogger(FileExportManager.class.getName()).log(Level.SEVERE, "problem write csv", ex);
//                }
        //salvando metricas no arquivo
//                path = Paths.get(METRICS_FILE_PATH);
//                try (BufferedWriter writer = Files.newBufferedWriter(path)) {
//                    for (Metric metric : matrix.computeMetric()) {
//                        writer.append(metric.toString());
//                        writer.append("\n");
//                    }
//                } catch (IOException ex) {
//                    Logger.getLogger(FileExportManager.class.getName()).log(Level.SEVERE, "problem write txt", ex);
//                }
//            }
    }

    protected void write(String text) {
        Path path = Paths.get("file.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(text);
        } catch (IOException ex) {
            Logger.getLogger(FileExportManager.class.getName()).log(Level.SEVERE,"problem write file",ex);
        }
    }

    public boolean nodeFromCall(Call call,Node node) {
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
}
