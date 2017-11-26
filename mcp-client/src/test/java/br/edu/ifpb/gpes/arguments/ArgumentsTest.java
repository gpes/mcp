/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.gpes.arguments;

import br.edu.ifpb.gpes.arguments.output.ArgParserOTP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author juan
 */
public class ArgumentsTest {

    ArgumentParser ap = new ArgumentParser();

    @Test
    public void wrongArguments() {
        ArgParserOTP output = ap.
                parse("--dir- main/src/ -from https://github.com/foo/bar --call true -m --export csv".split(" "));
        assertEquals(output.toString(), "ArgParserOTP{" + "dir=" + null + ", from=" + null + ", export=" + "csv" + ", calls=" + true + ", matrix=" + false + '}');
    }
    @Test
    public void onlyDirArgument() {
        ArgParserOTP output = ap.
                parse("--dir   a".split(" "));
        assertNotNull(output);
        assertEquals(output.getDir(), "");
        assertNotEquals(output.getDir(), "a");
    }
    @Test
    public void argumentWithHelp() {
        ArgParserOTP output = ap.
                parse("--help  --call true a".split(" "));
        assertNotNull(output);
    }

}
