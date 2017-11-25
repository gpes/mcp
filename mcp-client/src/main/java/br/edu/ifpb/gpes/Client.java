package br.edu.ifpb.gpes;

import ifpb.gpes.Call;
import ifpb.gpes.ExportManager;
import ifpb.gpes.Parse;
import ifpb.gpes.Project;
import ifpb.gpes.graph.AdapterGraph;
import ifpb.gpes.graph.Graph;
import ifpb.gpes.graph.Matrix;
import ifpb.gpes.jdt.ParseStrategies;
import ifpb.gpes.study.Study;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author juan
 */
public class Client {

    public static void main(String[] args) {
//      https://github.com/pmxa/plugin
        Project project = Project
                .root("/Users/job/Documents/dev/gpes/data/projects/pmxa-plugin/")
                .path("src/")
                .sources("src/")
                .filter(".java");
//        Project project = Project
//                .root("")
//                .path("../mcp-samples/src/main/java/ifpb/gpes/domain/LambdaAndAnonymous.java") // root
//                .sources("../mcp-samples/src/main/java/") // root - não obrigatorio
//                .filter(".java");

        Study.of(project)
                .with(Parse.with(ParseStrategies.JDT))
                .analysis(new ExportVoid())
                .execute();

    }

    static class ExportVoid implements ExportManager {
        
        

        @Override
        public void export(List<Call> elements) {
            AdapterGraph ag = new AdapterGraph();
            Graph graph = ag.apply(elements);
            Matrix matrix = graph.toMatrix();
            
            System.out.println(Arrays.toString(matrix.namesColumns()));
            System.out.println(matrix.valuesToString());
            matrix.computeMetric().forEach(System.out::println);
            new MatrixToJson(matrix).toJson();
            
        }

    }

}
