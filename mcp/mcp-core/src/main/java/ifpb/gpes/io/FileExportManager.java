package ifpb.gpes.io;

import ifpb.gpes.ExportManager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 07/07/2017, 15:33:48
 */
public abstract class FileExportManager implements ExportManager {

    protected String outputDir;

    public FileExportManager(String outputDir) {
        this.outputDir = outputDir;
    }

    public String handleOutputFilePath(String dir, String filename) {
        return dir.endsWith("/") ? dir + filename: dir + '/' + filename;
    }

    protected void write(String text, Path path) {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(text);
        } catch (IOException ex) {
            Logger.getLogger(FileExportManager.class.getName()).log(Level.SEVERE, "Problem writing a file in " + path.getFileName().toString() + " path.");
        }
    }
}
