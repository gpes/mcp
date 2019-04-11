package br.edu.ifpb.gpes;


import ifpb.gpes.Parse;
import ifpb.gpes.Project;
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

    @CommandLine.Option(names = {"-s", "--source"}, required = true, description = "The source path of the project.")
    private String sourcePath;

    public static void main(String[] args) {
        CommandLine.call(new CommandClient(), args);
    }

    @Override
    public Void call() throws Exception {
        Project project = Project
                .root(root)
                .path(sourcePath)
                .sources(sourcePath)
                .filter(".java");
        Study.of(project)
                .with(Parse.with(ParseStrategies.JDT))
                .analysis(new Client.ExportVoid(true, true))
                .execute();
        return null;
    }
}
