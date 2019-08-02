package ifpb.gpes;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 07/07/2017, 15:22:14
 */
public abstract class ExportManager {

    protected String outputDir;

    public ExportManager(String outputDir) {
        this.outputDir = outputDir;
    }

    public abstract void export(List<Call> elements);

    public String handleOutputFilePath(String dir, String filename) {
        return dir.endsWith("/") ? dir + filename: dir + '/' + filename;
    }

    public void write(String text, Path path) {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(text);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(ExportManager.class.getName()).log(Level.SEVERE, "Problem writing a file in " + path.getFileName().toString() + " path.");
        }
    }
}
