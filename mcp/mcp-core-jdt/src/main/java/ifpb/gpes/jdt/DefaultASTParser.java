package ifpb.gpes.jdt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ifpb.gpes.io.SmartFile;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 02/06/2017, 14:49:57
 */
public class DefaultASTParser {

    private final ASTParser parser = ASTParser.newParser(AST.JLS17);
    private final String[] sources;
    private Hashtable options = JavaCore.getOptions();
    private final String javaDefaultClasspath = System.getProperty("java.home") + "/lib/jrt-fs.jar";
    private final String[] classpath;

    public static DefaultASTParser from(String... sources) {

        if (sources == null) {
            throw new IllegalArgumentException("sources is null");
        }

        return new DefaultASTParser(sources, null);
    }

    public static DefaultASTParser from(String[] sources, String[] classpath) {

        if (sources == null) {
            throw new IllegalArgumentException("sources is null");
        }

        return new DefaultASTParser(sources, classpath);
    }

    private DefaultASTParser(String[] sources, String[] classpath) {
        this.sources = sources;
        if (classpath == null) {
            this.classpath = new String[] { javaDefaultClasspath };
        } else {
            List<String> jarsPathFromClasspath = getJarsPathFromClasspath(classpath);
            jarsPathFromClasspath.add(javaDefaultClasspath);
            this.classpath = jarsPathFromClasspath.toArray(String[]::new);
        }
        this.parser.setKind(ASTParser.K_COMPILATION_UNIT);
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_17);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_17);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_17);
    }

    public void updateUnitName(Path fileJava) {    
        try {
            byte[] readAllBytes = Files.readAllBytes(fileJava);
            String str = new String(readAllBytes);
            this.parser.setBindingsRecovery(true);
            this.parser.setResolveBindings(true);
            this.parser.setUnitName(fileJava.getFileName().toString());
            this.parser.setEnvironment(classpath, sources, new String[]{"UTF-8"}, true);
            this.parser.setSource(str.toCharArray());
        } catch (IOException ex) {
            Logger.getLogger(DefaultASTParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void acceptVisitor(ASTVisitor visitor) {
        //resolve problema do "unknow source"
        this.parser.setCompilerOptions(options);
        //--
        ASTNode createAST = parser.createAST(null);
        createAST.accept(visitor);
    }

    private List<String> getJarsPathFromClasspath(String... classpath) {
        List<Path> paths = new ArrayList<>();
        for (int i = 0; i < classpath.length; i++) {
            Stream<Path> stream = SmartFile.from(Paths.get(classpath[i])).extension(".jar");
            paths.addAll(stream.collect(Collectors.toList()));
        }
        return paths.stream().map(p -> p.toAbsolutePath().normalize().toString()).collect(Collectors.toList());
    }
}
