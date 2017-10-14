package ifpb.gpes.graph;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 */
public class Matrix {

    protected final int[][] matrix;

    private Matrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public Matrix(int numeroDeVertices) {
        this(new int[numeroDeVertices][numeroDeVertices]);
    }

    public Matrix() {
        this(0);
    }

    public int[][] toArray() {
        return this.matrix;
    }

//    public void generateMatrix() throws IOException, ExportException {
//        Node[] vertices = graph.vertexSet().toArray(new Node[]{});
//
//        matrix = new int[vertices.length][vertices.length];
//        ArrayList<Integer> notNullLinesIndex = new ArrayList<>();
//
//        for (int i = 0; i < vertices.length; i++) {
////            Set edgesOfVertice = graph.edgesOf(vertices[i]);
//            int soma = 0;
//            for (int j = 0; j < vertices.length; j++) {
//                Node node = vertices[i];
//                Node node2 = vertices[j];
//                DefaultWeightedEdge edge = graph.getEdge(node, node2);
//                if (edge != null) {
//                    matrix[i][j] = (int) graph.getEdgeWeight(edge);
//                    soma += matrix[i][j];
//                } else {
//                    matrix[i][j] = 0;
//                }
//                System.out.println("(" + i + "-" + j + ") (" + matrix[i][j] + ") (" + " )");
//            }
//            if (soma != 0) {
//                notNullLinesIndex.add(i);
//            }
//        }
//
//        int[][] refactoredmatrix = new int[notNullLinesIndex.size()][vertices.length];
//
//        notNullLinesIndex.forEach((line) -> {
//            refactoredmatrix[notNullLinesIndex.indexOf(line)] = matrix[line];
//        });
//
//        System.out.println("  A B C D E F G H I");
//        char[] letras = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I'};
//        for (int i = 0; i < vertices.length; i++) {
//            for (int j = 0; j < vertices.length; j++) {
//                if (j == 0) {
//                    if (i < 9) {
//                        System.out.print(letras[i]);
//                    }
//                }
//                System.out.print(" " + matrix[i][j]);
//            }
//            System.out.println("");
//        }
//
//        System.out.println('\n');
//
//        System.out.println("  A B C D E F G H I");
//        for (int i = 0; i < refactoredmatrix.length; i++) {
//            for (int j = 0; j < vertices.length; j++) {
//                if (j == 0) {
//                    if (i < 9) {
//                        System.out.print(letras[notNullLinesIndex.get(i)]);
//                    }
//                }
//                System.out.print(" " + refactoredmatrix[i][j]);
//            }
//            System.out.println("");
//        }
//
//    }
    public int weightSum() {
        if (matrix == null) {
            return 0;
        }
        return Arrays.stream(matrix).flatMapToInt(Arrays::stream).sum();
    }

    public List<Integer> linhasNaoNulas(int[][] dados) {
        List<Integer> linhasIndex = new ArrayList<>();
        for (int i = 0; i < dados.length; i++) {
            if (Arrays.stream(dados[i]).sum() > 0) {
                linhasIndex.add(i);
            }
        }
        return linhasIndex;
    }

//    TODO: It's not work 
    public Matrix matrizDeAdjacencia() {
        List<Integer> linhasNaoNulas = linhasNaoNulas(matrix);
        int[][] refactoredMatrix = new int[linhasNaoNulas.size()][matrix.length];
        for (int i = 0; i < refactoredMatrix.length; i++) {
            refactoredMatrix[i] = matrix[linhasNaoNulas.get(i)];
        }
        return new Matrix(refactoredMatrix);
    }

    public Cell cell(int linha, int coluna) {
        return new Cell(linha, coluna);
    }

    class Cell {

        private final int linha;
        private final int coluna;

        protected Cell(int linha, int coluna) {
            this.linha = linha;
            this.coluna = coluna;
        }

        public void set(int value) {
            matrix[linha][coluna] = value;
        }

        public int get() {
            return matrix[linha][coluna];
        }

        @Override
        public String toString() {
            return "(" + linha + "-" + coluna + ") (" + get() + ")";
        }
    }

    public String toStr() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println("");
        }
        return "";
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
                BigDecimal metric = new BigDecimal(0);
//                if (soma != 0) {
                    metric = new BigDecimal(peso).divide(new BigDecimal(soma), MathContext.DECIMAL32);
//                }
                System.out.println(i
                        + " -> " + j
                        + " peso: " + peso
                        + " soma: " + soma
                        + " metric " + metric);
            }
        }
    }

    private int soma(int coluna, int[][] matrix) {
        return IntStream.of(matrix[coluna]).sum();
    }

    private boolean naoConecta(int row, int col, int[][] matrix) {
//        return false;
        return matrix[row][col] == 0;
    }

}
/**
 *
 * public static void main(String[] args) throws java.lang.Exception { int[][]
 * data = new int[][]{ {1, 2, 3}, {4, 5, 6}, {7, 8, 9} };
 *
 * useArray(data);
 *
 * useObject(data);
 *
 * useStream(data); }
 *
 * private static void useArray(int[][] data) { System.out.println("----- usando
 * Array -----"); int size = data.length; Integer[][] array = IntStream.range(0,
 * size) .filter(linhas -> Arrays.stream(data[linhas]).sum() > 6) .mapToObj((int
 * linha) -> IntStream.range(0, size) .mapToObj(coluna -> { return
 * data[linha][coluna]; }) .toArray(Integer[]::new)) .toArray(Integer[][]::new);
 *
 * for (Integer[] integers : array) { for (Integer integer : integers) {
 * System.out.println(integer); } } }
 *
 * private static void useObject(int[][] data) { System.out.println("-----
 * usando Object -----"); Object[] ll = Arrays .stream(data) .map((a) -> a)
 * .toArray();
 *
 * for (Object l : ll) { //cada elemento referencia um outro array
 * System.out.println(l + " " + l.getClass().getSimpleName()); } }
 *
 * private static void useStream(int[][] data) { System.out.println("-----
 * usando Stream -----");
 *
 * Integer[][] toArray = Arrays.stream(data) .filter(linhas ->
 * Arrays.stream(linhas).sum() > 6) .map( r ->
 * Arrays.stream(r).mapToObj(Integer::new).toArray(Integer[]::new)
 * ).toArray(Integer[][]::new);
 *
 * for (Integer[] integers : toArray) { for (Integer integer : integers) {
 * System.out.println(integer); } } }
 *
 *
 */
