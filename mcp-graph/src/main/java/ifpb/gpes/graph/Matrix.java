package ifpb.gpes.graph;

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
    private final String[] columns;

    public Matrix(int[][] matrix) {
        this.matrix = matrix;
        this.columns = new String[matrix.length];
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

    public int sumAllWeight() {
        if (matrix == null) {
            return 0;
        }
        return Arrays.stream(matrix).flatMapToInt(Arrays::stream).sum();
    }

//    public int sumRow(int row) {
//        if (matrix == null) {
//            return 0;
//        }
//
//        return IntStream.range(0, matrix.length)
//                .filter(j -> j == row)
//                .sum();
//    }
    public boolean conectado(int row) {
        int soma = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if ((i == row) || (row == j)) {
                    soma = soma + matrix[i][j];
                }
            }
        }
        return soma > 0;
    }

    public String valuesToString() {
        StringBuilder builder = new StringBuilder();
        for (int[] row : matrix) {
            for (int j = 0; j < matrix.length; j++) {
                builder.append(row[j]).append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public String[] namesColumns() {
        return this.columns;
    }

    public List<Metric> computeWithMetric(StrategyMetric strategy) {
        List<Metric> metrics = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                int weight = matrix[i][j];
                if (notConnected(i, j)) {//os nos estão conectados
                    continue;
                }
                int sum = sumCallsTo(j) + weight; //quantos nos partem dele
                metrics.add(new Metric(String.valueOf(i), String.valueOf(j),
                        weight, sum, strategy));
            }
        }
        return metrics;
    }

    public List<Metric> computeMetric() {
        return computeWithMetric(new DefaultStrategyMetric());
    }

//    TODO: It's not work 
    public Matrix matrizDeAdjacencia() {
        List<Integer> linhasNaoNulas = rowsNotNull(matrix);
        int[][] refactoredMatrix = new int[linhasNaoNulas.size()][matrix.length];
        for (int i = 0; i < refactoredMatrix.length; i++) {
            refactoredMatrix[i] = matrix[linhasNaoNulas.get(i)];
        }
        return new Matrix(refactoredMatrix);
    }

    public Cell cell(int row, int column) {
        return new Cell(row, column);
    }

    public void updateNameColumn(int column, String name) {
        this.columns[column] = name;
    }

    private List<Integer> rowsNotNull(int[][] dados) {
        List<Integer> rowsIndex = new ArrayList<>();
        for (int i = 0; i < dados.length; i++) {
            if (Arrays.stream(dados[i]).sum() > 0) {
                rowsIndex.add(i);
            }
        }
        return rowsIndex;
    }

    private int sumCallsTo(int column) {
        return IntStream.of(matrix[column]).sum();
    }

    private boolean notConnected(int row, int col) {
        return this.matrix[row][col] == 0;
    }

    class Cell {

        private final int row;
        private final int column;

        protected Cell(int row, int column) {
            this.row = row;
            this.column = column;
        }

        public void set(int value) {
            matrix[row][column] = value;
        }

        public int get() {
            return matrix[row][column];
        }

        @Override
        public String toString() {
            return "(" + row + "-" + column + ") (" + get() + ")";
        }
    }

}
