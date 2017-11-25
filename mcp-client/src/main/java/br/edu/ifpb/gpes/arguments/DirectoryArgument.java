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
public class DirectoryArgument extends AbstractArgument {

    private final String DIR_SHORT_FLAG = "-d";
    private final String DIR_FLAG = "--dir";

    @Override
    public void work(String argument, ArgParserOTP output) {
        output.setDir(argument);
    }

    @Override
    public void parseArgument(String inputFlag, String argument, ArgParserOTP output) {
        super.parseArgument(inputFlag, argument, DIR_SHORT_FLAG, DIR_FLAG, output);
    }

    @Override
    public void getHelp() {
        System.out.println("-d or --dir -> argumento usado para informar o path das classes do projeto que será analisado");
    }

}
