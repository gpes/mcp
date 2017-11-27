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
public class HelpArgument extends AbstractArgument{

    private final String HELP_SHORT_FLAG = "-h";
    private final String HELP_FLAG = "--help";
    
    @Override
    public void work(String argument, ArgParserOTP output) {
        output = null;
    }

    @Override
    public void parseArgument(String inputFlag, String argument, ArgParserOTP output) {
        super.parseArgument(inputFlag, argument, HELP_SHORT_FLAG, HELP_FLAG, output);
    }

    @Override
    public void getHelp() {
        System.out.println("-h or --help   -> use this argument to see the info of others arguments");
    }
    
}
