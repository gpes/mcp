
import ifpb.gpes.Call;
import ifpb.gpes.Parse;
import ifpb.gpes.Project;
import ifpb.gpes.graph.Matrix;
import ifpb.gpes.graph.SmartDirectGraph;
import ifpb.gpes.jdt.ParseStrategies;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 11/10/2017, 14:54:23
 */
public class MainMatrix {

    public static void main(String[] args) {

        String sources = "../mcp-samples/src/main/java/";
        Project project = Project
                .root("")
                .path(sources + "ifpb/gpes/domain/jcf/Source.java")
                .sources(sources)
                .filter(".java");

        List<Call> calls = Parse.with(ParseStrategies.JDT).from(project);
        SmartDirectGraph dg = new SmartDirectGraph();
        calls.stream()
                //                .filter((call) -> call.getCallMethod() != null)
                .peek(c -> System.out.println(c.callGraph())) //apenas imprime
                .forEach(dg::adicionarNos);
//        
        Matrix matrix = dg.generateMatrix();
        matrix.toStr();
        matrix.metric();

//        dg.getGraph().edgeSet().forEach((DefaultWeightedEdge w) -> System.out.println(w));
//        System.out.println(dg.getGraph());
        //        System.out.println(matrix.toStr());

//        int[][] matrix = {
//            {0, 2, 0, 0, 0},
//            {0, 0, 0, 0, 1},
//            {0, 1, 0, 3, 0},
//            {0, 0, 0, 0, 1},
//            {0, 0, 0, 0, 0}
//        };
//        new CalcMatrix().metric();
//                Study.of(project)
//                        .with(Parse.with(ParseStrategies.JDT))
//                        .toFile("james").execute();
    }

    static class CalcMatrix {

        String colunas = "ABCDEFGHIJKLMNOPQRSTUWVYZ";
        int[][] matrix = {
            {0, 2, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 2},
            {0, 1, 0, 3, 0, 0},
            {0, 0, 0, 0, 1, 2},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0}
        };

        public CalcMatrix() {
        }

        public CalcMatrix(int[][] matrix) {
            this.matrix = matrix;
        }

        public void metric() {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length; j++) {
                    int peso = matrix[i][j];
                    //os nos estão conectados
                    if (naoConecta(i, j, matrix)) {
                        continue;
                    }
                    //quantos nos partem dele
                    int soma = soma(j, matrix) + peso;
                    BigDecimal metric = new BigDecimal(peso).divide(new BigDecimal(soma), MathContext.DECIMAL32);
//                    System.out.println(colunas.charAt(i)
//                            + " -> " + colunas.charAt(j)
//                            + " peso: " + peso
//                            + " soma: " + soma
//                            + " metric " + metric);
                    System.out.println(colunas.charAt(i)
                            + "," + colunas.charAt(j)
                            + "," + peso
                            + "," + soma
                            + "," + metric
                            + ",");
//                    System.out.println((i+1)
//                            + " " + (j+1)
//                            + " " + peso
//                            + " " + soma
//                            + " " + metric
//                            + " ");
                }
            }
        }

        private int soma(int coluna, int[][] matrix) {
            return IntStream.of(matrix[coluna]).sum();
        }

        private boolean naoConecta(int row, int col, int[][] matrix) {
            return matrix[row][col] == 0;
        }
    }

}
