package ifpb.gpes;

import ifpb.gpes.io.SmartFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 05/07/2017, 14:30:09
 */
public class Project {

    private String extensions = "";
    private SmartFile smart;
    private ProjectType type = new DefaultProjectType();
    private final String root;
    private String name = UUID.randomUUID().toString();

    protected Project(String dir) {
        this.root = dir;
    }

    public static Project root(String dir) {
        return new Project(dir);
    }

    public Project type(ProjectType type) {
        this.type = type;
        return this;
    }

    public Project path(String path) {
        this.smart = SmartFile.from(Paths.get(root + path));
        return this;
    }

    public Project name(String name) {
        this.name = name;
        return this;
    }

    public Project sources(String string) {
        this.type.addSources(string);
        return this;
    }

    public Project classpath(String[] classpath) {
        String[] correctedCp = Stream.ofNullable(classpath)
            .map(p -> Paths.get(smart.path().toAbsolutePath().toString(), p).toAbsolutePath().toString())
            .toList()
            .toArray(String[]::new);
        this.type.addClasspath(correctedCp);
        return this;
    }

    public Stream<Path> files() {
        return this.smart.extension(extensions);
    }

    public String sources() {
        return this.root + this.type.sources();
    }

    public String[] classpath() {
        return this.type.classpath();
    }

    public String name() {
        return this.name;
    }

    public Project filter(String extension) {
        this.extensions = extension;
        return this;
    }

    private static class DefaultProjectType implements ProjectType {

        private String sources;
        private String[] classpath;

        @Override
        public void addSources(String source) {
            this.sources = source;
        }

        @Override
        public String sources() {
            return this.sources;
        }

        @Override
        public void addClasspath(String[] classpath) {
            this.classpath = classpath;
        }

        public String[] classpath() {return this.classpath;}
    }
}
