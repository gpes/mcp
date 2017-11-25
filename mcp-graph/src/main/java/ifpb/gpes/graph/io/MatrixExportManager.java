/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.gpes.graph.io;

import ifpb.gpes.Call;
import ifpb.gpes.ExportManager;
import ifpb.gpes.graph.AdapterGraph;
import ifpb.gpes.graph.Graph;
import ifpb.gpes.graph.Matrix;
import ifpb.gpes.io.FileExportManager;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juan
 */
public class MatrixExportManager implements ExportManager {

    private final String MATRIX_FILE_PATH = "../matrix.csv";

    @Override
    public void export(List<Call> elements) {
        AdapterGraph ag = new AdapterGraph();
        Graph graph = ag.apply(elements);
        Matrix matrix = graph.toMatrix();

        write(matrix.toArray());
    }

    private void write(int[][] matrix) {
        Path path = Paths.get(MATRIX_FILE_PATH);

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (int[] line : matrix) {
                for (int column : line) {
                    writer.append(String.valueOf(column));
                    writer.append(",");
                }
                writer.append("\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(FileExportManager.class.getName()).log(Level.SEVERE, "problem write csv", ex);
        }
    }

}
