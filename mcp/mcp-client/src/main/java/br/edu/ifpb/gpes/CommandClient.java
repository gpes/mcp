package br.edu.ifpb.gpes;

import br.edu.ifpb.gpes.export.ExportStrategy;
import ifpb.gpes.Parse;
import ifpb.gpes.Project;
import ifpb.gpes.jcf.io.CategoryMethodExportManager;
import ifpb.gpes.jdt.ParseStrategies;
import ifpb.gpes.study.Study;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import picocli.CommandLine;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.Callable;

@CommandLine.Command(description = "Parse a project.", name = "client", version = {"1.0"})
public class CommandClient implements Callable<Void> {

    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true, description = "Display help message.")
    private boolean usageHelp;

    @CommandLine.Option(names = {"-v", "--version"}, versionHelp = true, description = "Display version number.")
    private boolean versionHelp;

    @CommandLine.Option(names = {"-r", "--root"}, required = true, description = "The rooth path of the project that will be parsed.")
    private String root;

    @CommandLine.Option(names = {"-s", "--source"}, required = true, description = "The java source path of the project e.g.: 'src/main/java'.")
    private String source;

    @CommandLine.Option(names = {"-p", "--path"}, required = true, description = "The path of the file(s) that you want to be parsed.")
    private String path;

    @CommandLine.Option(names = {"-o", "--output"}, required = true, description = "The path where the generated outputs will be created. If not exist or found, it will be created.")
    private String outputDir;

    @CommandLine.Option(names = {"-t", "--strategy"}, required = true, description = "Select the strategy used to process the list of call objects" +
            "\nThe following strategies are available:\n\t*BROKE (Find confinement brokens)" +
            "\n\t*CATEGORYMETHOD (Categorize the methods used in the collections, grouped by method)" +
            "\n\t*CATEGORYINTERFACE (Categorize the methods used in the collections, grouped by category)" +
            "\n\t*PRINT (Print in the console all method calls)\r")
    private String strategy;

    public static void main(String[] args) {
        CommandLine.call(new CommandClient(), args);
    }

    @Override
    public Void call() {
        Project project = Project
                .root(root)
                .path(path)
                .sources(source)
                .filter(".java");
        try {
            Study.of(project)
                    .with(Parse.with(ParseStrategies.JDT))
                    .analysis(ExportStrategy.valueOf(strategy).exportFactory(outputDir))
                    .execute();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }
}
