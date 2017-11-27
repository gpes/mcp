package br.edu.ifpb.gpes;

import br.edu.ifpb.gpes.arguments.ArgumentParser;
import br.edu.ifpb.gpes.arguments.output.ArgParserOTP;
import ifpb.gpes.Call;
import ifpb.gpes.ExportManager;
import ifpb.gpes.Parse;
import ifpb.gpes.Project;
import ifpb.gpes.graph.AdapterGraph;
import ifpb.gpes.graph.Graph;
import ifpb.gpes.graph.Matrix;
import ifpb.gpes.io.PrintOutManager;
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
//        Project project = Project
//                .root("/Users/job/Documents/dev/gpes/data/projects/pmxa-plugin/")
//                .path("src/")
//                .sources("src/")
//                .filter(".java");
//        Project project = Project
//                .root("")
//                .path("../mcp-samples/src/main/java/ifpb/gpes/domain/LambdaAndAnonymous.java") // root
//                .sources("../mcp-samples/src/main/java/") // root - não obrigatorio
//                .filter(".java");

//        Study.of(project)
//                .with(Parse.with(ParseStrategies.JDT))
//                .analysis(new ExportVoid(true, true))
//                .execute();
        ArgParserOTP output = new ArgumentParser().
                parse(args);
        if (output != null) {
            InputOfStudy.from(output).execute();
        }
    }

    static class ExportVoid implements ExportManager {

        private final ExportManager origin = new PrintOutManager();
        private final boolean calls;
        private final boolean matriz;

        private ExportVoid(boolean CallsEnabled, boolean MatrixEnabled) {
            this.calls = CallsEnabled;
            this.matriz = MatrixEnabled;
        }

        @Override
        public void export(List<Call> elements) {

            if (calls) {
                origin.export(elements);
            }

            if (matriz) {
                Graph graph = new AdapterGraph().apply(elements);
                Matrix matrix = graph.toMatrix();
                System.out.println(Arrays.toString(matrix.namesColumns()));
                System.out.println(matrix.valuesToString());
                matrix.computeMetric().forEach(System.out::println);
                new MatrixToJson(matrix).toJson();
            }
        }

    }

    static class InputOfStudy extends Study {

        private final ArgParserOTP arg;

        private InputOfStudy(ArgParserOTP arg, Project project) {
            super(project);
            this.arg = arg;
        }

        public static Study from(ArgParserOTP arg) {
            return new InputOfStudy(arg,
                    Project
                            .root(arg.getFrom())
                            .path(arg.getDir())
                            .sources(arg.getSource())
                            .filter(".java"));
        }

        @Override
        public void execute() {
            super.with(Parse.with(ParseStrategies.JDT))
                    .analysis(new ExportVoid(arg.CallsEnabled(), arg.MatrixEnabled()))
                    .execute();
        }
    }
}
