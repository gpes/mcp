/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.gpes.arguments.output;

/**
 *
 * @author juan
 */
public class ArgParserOTP {

    private String dir;
    private String from;
    private String export;
    private boolean calls;
    private boolean matrix;
    private final String SOURCE = "src/";

    public String getDir() {
        return dir;
    }

    public String getFrom() {
        return from;
    }

    public String getExport() {
        return export;
    }

    public boolean CallsEnabled() {
        return calls;
    }

    public boolean MatrixEnabled() {
        return matrix;
    }

    public ArgParserOTP setDir(String dir) {
        this.dir = dir;
        return this;
    }

    public ArgParserOTP setFrom(String from) {
        this.from = from;
        return this;
    }

    public ArgParserOTP setExport(String export) {
        this.export = export;
        return this;
    }

    public ArgParserOTP setCalls(boolean calls) {
        this.calls = calls;
        return this;
    }

    public ArgParserOTP setMatrix(boolean matrix) {
        this.matrix = matrix;
        return this;
    }

    public String getSource() {
        return this.SOURCE;
    }
    
    @Override
    public String toString() {
        return "ArgParserOTP{" + "dir=" + dir + ", from=" + from + ", export=" + export + ", calls=" + calls + ", matrix=" + matrix + '}';
    }

}
