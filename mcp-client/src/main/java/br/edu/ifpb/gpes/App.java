package br.edu.ifpb.gpes;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 21/11/2017, 11:03:35
 */
public class App {
    public static void main(String[] args) {
        String[] arg = "-dir src/  -from https://github.com/pmxa/plugin".split(" ");
        for (String string : arg) {
            System.out.println("string = " + string);
        }
        
    }
}
