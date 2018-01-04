package br.edu.ifpb.gpes;

import br.edu.ifpb.gpes.arguments.output.ArgParserOTP;
import ifpb.gpes.Call;
import ifpb.gpes.ExportManager;
import ifpb.gpes.Parse;
import ifpb.gpes.Project;
import ifpb.gpes.filter.FilterByMethod;
import ifpb.gpes.filter.FilterClassType;
import ifpb.gpes.graph.AdapterGraph;
import ifpb.gpes.graph.Graph;
import ifpb.gpes.graph.Matrix;
import ifpb.gpes.graph.Metric;
import ifpb.gpes.graph.Node;
import ifpb.gpes.io.FileExportManager;
import ifpb.gpes.io.FilteredFileExportManager;
import ifpb.gpes.jdt.ParseStrategies;
import ifpb.gpes.study.Study;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
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
public class Client {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        //https://github.com/Rogiel/ufrgs-oop-projeto-final #
        //https://github.com/Horoneru/ProjetOffresStage #
        //https://github.com/Peaupote/POOIG #
        //projetos analisados
        //https://github.com/Donnervoegel/java  
        //https://github.com/DaniloRaniery/Projeto 
        //https://github.com/natansevero/DungeonsAndDragons 
        //https://github.com/wensttay/poo-lanchonete 
        //https://github.com/juanpablolvl99/PSD 
        //https://github.com/Lyndemberg/HiDiary 
        //https://github.com/ThiagoYure/Projeto-POO 
        //https://github.com/lcsvcn/xadrez 
        //https://github.com/luhanlacerda/projetogym 
        //https://github.com/ricarterodrigues/AgendaNota10-2 
        //https://github.com/LukasGrudtner/Yu-Gi-Oh 
        //https://github.com/KaioTM/Supermercado 
        //https://github.com/brunowdev/fbgym-poo2 
        //https://github.com/Lionhear21/Project3POO 
        //https://github.com/gustavomeloGH/Projeto-POO-Livre-Leitura-Biblioteca
        //https://github.com/rodrigobentorodrigues/APS
        //https://github.com/namtan8888/Project
        //https://github.com/jgvmonteiro/POOFindConcertTicket 
        //https://github.com/wensttay/poo-biblioteca 
        //https://github.com/magniffico/OOTanks 
        //https://github.com/kellytnguyen/CS356_Project2 
        //https://github.com/LiamDotPro/Access-Control-System 
        //https://github.com/prateek-khatri/OOADProject 
        //https://github.com/rstancioiu/Dev-OO 
        //https://github.com/Confirm4Crit/OODShoppingCartProject
        //https://github.com/gpreis/agenda
        //https://github.com/ayushkr19/zephyr-oop-group-2
        //https://github.com/tylernolan/OOPDA_BikeShop
        //https://github.com/rodrigobentorodrigues/POO
        Project project = Project
                .root("../mcp-samples/src/main/java/")
                .path("ifpb/gpes/domain/AnonymousClass.java")
                .sources("")
                .filter(".java");
        Study.of(project)
                .with(Parse.with(ParseStrategies.JDT))
                .analysis(new ExportVoid(true, true))
                .execute();

    }

    static class ExportVoid implements ExportManager {

        private final String MATRIX_FILE_PATH = "../matrix.csv";
        private final String METRICS_FILE_PATH = "../metrics";
        private final ExportManager origin = FilteredFileExportManager.name("file");
        private final boolean calls;
        private final boolean matriz;

        private ExportVoid(boolean CallsEnabled, boolean MatrixEnabled) {
            this.calls = CallsEnabled;
            this.matriz = MatrixEnabled;
        }

        @Override
        public void export(List<Call> elements) {

            if (calls) {
                origin.export(elements);
            }
            if (matriz) {
                Path path = Paths.get(MATRIX_FILE_PATH);
                Graph graph = new AdapterGraph().apply(elements);
                List<Call> candidates = graph.getCandidates();
                List<Predicate<Call>> predicates = Arrays
                        .asList(new FilterByMethod(), new FilterClassType("java.util.Collection"));
                Predicate<Call> compositePredicate
                        = predicates.stream().reduce(w -> true, Predicate::and);
                elements = elements.stream().filter(compositePredicate).collect(Collectors.toList());
                List<Integer> indices = new ArrayList<>();
                Matrix matrix = graph.toMatrix();
                List<Node> columnsList = Arrays.asList(matrix.getColumns());
                for (Call candidate : candidates) {
                    if (elements.contains(candidate)) {
                        for (Node node : columnsList) {
                            if(nodeFromCall(candidate, node))
                                indices.add(columnsList.indexOf(node));
                        }
                    }
                }
                for (Integer indice : indices) {
                    System.out.print(indice + " - ");
                }
                System.out.println(Arrays.toString(matrix.namesColumns()));
                System.out.println(matrix.valuesToString());
                matrix.computeMetric().forEach(System.out::println);
                new MatrixToJson(matrix).toJson(indices);
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
                    Logger.getLogger(FileExportManager.class.getName()).log(Level.SEVERE, "problem write csv", ex);
                }
                //salvando metricas no arquivo
                path = Paths.get(METRICS_FILE_PATH);
                try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                    for (Metric metric : matrix.computeMetric()) {
                        writer.append(metric.toString());
                        writer.append("\n");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(FileExportManager.class.getName()).log(Level.SEVERE, "problem write txt", ex);
                }
            }
        }

        public boolean nodeFromCall(Call call, Node node) {
            if(!call.getMethodName().equals(node.getMethodName()))
                return false;
            if(!call.getClassType().equals(node.getClassName()))
                return false;
            if(!call.getReturnType().equals(node.getReturnType()))
                return false;
            return true;
        }
    }

    static class InputOfStudy extends Study {

        private final ArgParserOTP arg;

        private InputOfStudy(ArgParserOTP arg, Project project) {
            super(project);
            this.arg = arg;
        }

        public static Study from(ArgParserOTP arg) {
            return new InputOfStudy(arg,
                    Project
                            .root(arg.getFrom())
                            .path(arg.getDir())
                            .sources(arg.getSource())
                            .filter(".java"));
        }

        @Override
        public void execute() {
            super.with(Parse.with(ParseStrategies.JDT))
                    .analysis(new ExportVoid(arg.CallsEnabled(), arg.MatrixEnabled()))
                    .execute();
        }
    }
}
