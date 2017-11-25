/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.gpes.arguments;

import br.edu.ifpb.gpes.arguments.output.ArgParserOTP;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author juan
 */
public class ArgumentsTest {

    ArgumentParser ap = new ArgumentParser();

    @Test
    public void wrongArguments() {
        ArgParserOTP output = ap.parse("--dir- main/src/ -from https://github.com/foo/bar --call true -m --export csv".split(" "));
        assertEquals(output.toString(), "ArgParserOTP{" + "dir=" + null + ", from=" + null + ", export=" + "csv" + ", calls=" + true + ", matrix=" + false + '}');
    }

}
