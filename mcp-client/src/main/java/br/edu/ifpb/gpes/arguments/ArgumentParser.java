/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.gpes.arguments;

import br.edu.ifpb.gpes.arguments.output.ArgParserOTP;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author juan
 */
public class ArgumentParser {

    private final List<AbstractArgument> argumentTypeList;

    {
        argumentTypeList = new ArrayList<>();
        argumentTypeList.add(new HelpArgument());
        argumentTypeList.add(new DirectoryArgument());
        argumentTypeList.add(new RepositoryArgument());
        argumentTypeList.add(new CallArgument());
        argumentTypeList.add(new MatrixArgument());
        argumentTypeList.add(new ExportArgument());
    }

    private final Map<String, String> arguments;

    public ArgumentParser() {
        arguments = new HashMap();
    }

    public ArgParserOTP parse(String[] args) {

        ArgParserOTP output = new ArgParserOTP();

        populate(args);

        arguments.forEach((k, v) -> {System.out.println(k + "->" + v + ";");});
        
        if (hasHelpFlag()) {
            showHelp();
            return null;
        }

        if (!dirAndFromArePresent()) {
            System.out.println("some arguments that are required were missing\nsee -h or --help");
            return null;
        } else if (dirAndFromAreEmpty()){
            System.out.println("some arguments that are required were empty\nsee -h or --help");
            return null;
        }

        arguments.forEach((k, v) -> {
            argumentTypeList.forEach((ar) -> ar.parseArgument(k, v, output));
        });

        if (!areValidPaths(output)) {
            System.out.println("it's not possible find the given paths");
            return null;
        }

        return output;
    }

    private void showHelp() {
        argumentTypeList.forEach(Argument::getHelp);
    }

    private void populate(String[] args) {
        if (args.length == 1) {
            arguments.put(args[0], "");
            return;
        }
        for (int i = 0; i < args.length - 1; i++) {
            if (isFlag(args[i])) {
                if (!isFlag(args[i + 1])) {
                    arguments.put(args[i], args[i + 1]);
                } else {
                    arguments.put(args[i], "");
                }
            }
        }
    }

    private boolean dirAndFromArePresent() {
        boolean dir = arguments.containsKey("-d") || arguments.containsKey("--dir");
        boolean from = arguments.containsKey("-f") || arguments.containsKey("--from");
        return dir && from;
    }

    private boolean hasHelpFlag() {
        return arguments.containsKey("-h") || arguments.containsKey("--help");
    }

    private boolean isFlag(String arg) {
        return arg.matches("^[-]{2}(?!-)[^-]+$") || arg.matches("^[-](?!-)[^-]+$");
    }

    private boolean areValidPaths(ArgParserOTP output) {
        Path path = Paths.get(output.getFrom() + output.getDir() + output.getSource());
        System.out.println(path);
        return path.toFile().exists();
    }

    private boolean dirAndFromAreEmpty() {
        String dirValue = arguments.get("-d");
        if(arguments.get("--dir") != null)
            dirValue = arguments.get("--dir");
        
        String fromValue = arguments.get("-f");
        if(arguments.get("--from") != null)
            fromValue = arguments.get("--from");
        
        return dirValue.equals("") || fromValue.equals("");
    }
}
