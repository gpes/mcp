package br.edu.ifpb.gpes;

import ifpb.gpes.Parse;
import ifpb.gpes.Project;
import ifpb.gpes.jcf.io.CategoryExportManager;
import ifpb.gpes.jdt.ParseStrategies;
import ifpb.gpes.study.Study;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(description = "Parse a project.", name = "client", version = {"1.0"})
public class CommandClient implements Callable<Void> {

    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true, description = "Display help message.")
    private boolean usageHelp;

    @CommandLine.Option(names = {"-v", "--version"}, versionHelp = true, description = "Display version number.")
    private boolean versionHelp;

    @CommandLine.Option(names = {"-r", "--root"}, required = true, description = "The rooth path of the project that will be parsed.")
    private String root;

    @CommandLine.Option(names = {"-src", "--source"}, required = true, description = "The java source path of the project e.g.: 'src/main/java'.")
    private String source;

    @CommandLine.Option(names = {"-p", "--path"}, required = true, description = "The path of the file(s) that you want to be parsed.")
    private String path;

    @CommandLine.Option(names = {"-o", "--output"}, required = true, description = "The path where the generated outputs will be created. If not exist or found, it will be created.")
    private String outputDir;

    @CommandLine.Option(names = {"-s", "--strategy"}, required = true, description = "Select the strategy used to process the list of call objects.")
    private String strategy;

    public static void main(String[] args) {
        args = new String[]{"-r", "/home/shotaro/trash-projects/quickserver-1.4.7/", "-src", "src/main/", "-p", "src/main/", "-o", "."};
        CommandLine.call(new CommandClient(), args);
    }

    @Override
    public Void call() {
        Project project = Project
                .root(root)
                .path(path)
                .sources(source)
                .filter(".java");
        Study.of(project)
                .with(Parse.with(ParseStrategies.JDT))
                .analysis(new CategoryExportManager(outputDir))
                .execute();
        return null;
    }
}
