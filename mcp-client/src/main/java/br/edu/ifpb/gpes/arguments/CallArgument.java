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
public class CallArgument extends AbstractArgument {

    private final String CALL_SHORT_FLAG = "-c";
    private final String CALL_FLAG = "--call";

    @Override
    public void work(String argument, ArgParserOTP output) {
        if(argument.equals("false") || argument.isEmpty())
            output.setCalls(false);
        else
            output.setCalls(true);
    }

    @Override
    public void parseArgument(String inputFlag, String argument, ArgParserOTP output) {
        super.parseArgument(inputFlag, argument, CALL_SHORT_FLAG, CALL_FLAG, output);
    }

    @Override
    public void getHelp() {
        System.out.println("-c or --call   -> argumento usado para informar se a lista de chamadas será adicionada no output da ferramenta \n\t\t  valor default: false");
    }
    
}
