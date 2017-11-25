/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.gpes.arguments;

import br.edu.ifpb.gpes.arguments.output.ArgParserOTP;
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

        if (hasHelpFlag()) {
            showHelp();
            return null;
        }

        arguments.forEach((k, v) -> {
            argumentTypeList.forEach((ar) -> ar.parseArgument(k, v, output));
        });       
        arguments.forEach((k, v)-> System.out.println(k + " " + v));
        return output;
    }

    private void showHelp() {
        argumentTypeList.forEach(Argument::getHelp);
    }

    private void populate(String[] args) {
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

    private boolean hasHelpFlag() {
        return arguments.containsKey("-h") || arguments.containsKey("--help");
    }

    private boolean isFlag(String arg) {
        return arg.matches("^[-]{2}(?!-)[^-]+$") || arg.matches("^[-](?!-)[^-]+$");
    }
}
