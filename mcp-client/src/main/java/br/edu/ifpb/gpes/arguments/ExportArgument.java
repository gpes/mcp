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
public class ExportArgument extends AbstractArgument {

    private final String EXPORT_SHORT_FLAG = "-e";
    private final String EXPORT_FLAG = "--export";

    @Override
    public void work(String argument, ArgParserOTP output) {
        output.setExport(argument);
    }

    @Override
    public void parseArgument(String inputFlag, String argument, ArgParserOTP output) {
        super.parseArgument(inputFlag, argument, EXPORT_SHORT_FLAG, EXPORT_FLAG, output);
    }

    @Override
    public void getHelp() {
        System.out.println("-e or --export -> argumento usado para informar o tipo do output gerado pela ferramenta \n\t\t  valor default: txt");
    }
    
}
