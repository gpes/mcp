/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.gpes.arguments;

import br.edu.ifpb.gpes.arguments.output.ArgParserOTP;

/**
 *
 * @author juan
 */
public class RepositoryArgument extends AbstractArgument {

    private final String REPO_SHORT_FLAG = "-f";
    private final String REPO_FLAG = "--from";


    @Override
    public void work(String argument, ArgParserOTP output) {
        output.setFrom(argument);
    }

    @Override
    public void parseArgument(String inputFlag, String argument, ArgParserOTP output) {
        super.parseArgument(inputFlag, argument, REPO_SHORT_FLAG, REPO_FLAG, output);
    }

    @Override
    public void getHelp() {
        System.out.println("-f or --from   -> use este argumento para informar o link do projeto a ser analisado (required)");
    }

}
