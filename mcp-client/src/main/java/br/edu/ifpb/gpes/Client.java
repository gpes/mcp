/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifpb.gpes;

import ifpb.gpes.Call;
import ifpb.gpes.ExportManager;
import ifpb.gpes.Parse;
import ifpb.gpes.Project;
import ifpb.gpes.graph.Matrix;
import ifpb.gpes.graph.SmartDirectGraph;
import ifpb.gpes.jdt.ParseStrategies;
import ifpb.gpes.study.Study;
import java.util.List;

/**
 *
 * @author juan
 */
public class Client {
    private static final String sources = "../mcp-samples/src/main/java/";

    public static void main(String[] args) {

        Project project = Project
                .root("")
                .path(sources + "ifpb/gpes/domain/LambdaWithArguments.java")
                .sources(sources)
                .filter(".java");
        
        Study.of(project)
                .with(Parse.with(ParseStrategies.JDT))
                .analysis(new ExportVoid())
                .execute();

    }
    
    static class ExportVoid implements ExportManager{

        SmartDirectGraph graph = new SmartDirectGraph();
        
        @Override
        public void export(List<Call> elements) {
            elements.stream().forEach(graph::adicionarNos);
            Matrix generateMatrix = graph.generateMatrix();
            System.out.println(generateMatrix.toStr());
            graph.getGraph().vertexSet().stream().forEach(System.out::println);
            generateMatrix.metric();
        }
        
    }

}
