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
public class MatrixArgument extends AbstractArgument {

    private final String MATRIX_SHORT_FLAG = "-m";
    private final String MATRIX_FLAG = "--matrix";
    
    @Override
    public void work(String argument, ArgParserOTP output) {
        if(argument.equals("false") || argument.isEmpty())
            output.setMatrix(false);
        else
            output.setMatrix(true);
    }

    @Override
    public void parseArgument(String inputFlag, String argument, ArgParserOTP output) {
        super.parseArgument(inputFlag, argument, MATRIX_SHORT_FLAG, MATRIX_FLAG, output);
    }

    @Override
    public void getHelp() {
        System.out.println("-m or --matrix -> argumento usado para informar se a matrix será adicionada no output da ferramenta \n\t\tvalor default: false");
    }
    
}
