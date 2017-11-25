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
public abstract class AbstractArgument implements Argument {
    
    protected void parseArgument(String inputFlag, String argument, String shortFlag, String flag, ArgParserOTP output) {
        if (inputFlag.equals(flag) || inputFlag.equals(shortFlag)) {
            this.work(argument, output);
        }
    }

    public abstract void work(String argument, ArgParserOTP output);

}
