package br.edu.ifpb.gpes;

import ifpb.gpes.Call;
import ifpb.gpes.ExportManager;
import ifpb.gpes.Project;
import ifpb.gpes.io.PrintOutManager;
import ifpb.gpes.jdt.DefaultASTParser;
import ifpb.gpes.jdt.DefaultVisitor;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 29/03/2019, 08:06:36
 */
public class NaVera {

    public static void main(String[] args) {
        List<Call> elements = new ArrayList<>();
        ASTVisitor visitor = new DefaultVisitor(elements);
        Project project = Project
            .root("/Users/job/Desktop/collections-3.2.1/")
            .path("src/java/")
            .sources("src/java/")
            .filter(".java");
        Default parser = Default.from(project.sources());
        project.files().forEach(p -> {
            parser.updateUnitName(p);
            parser.acceptVisitor(visitor);
        });

        ExportManager strategy = new PrintOutManager();
        strategy.export(elements);

    }
}

class Default {

    private final ASTParser parser = ASTParser.newParser(AST.JLS8);
    private final String[] sources;
    private Hashtable options = JavaCore.getOptions();
    private final String[] classpath = {
        System.getProperty("java.home") + "/lib/rt.jar",
        "/Users/job/Desktop/jext-5.0/lib/*.jar"
    };

    public static Default createParse(String[] sources) {

        if (sources == null) {
            throw new IllegalArgumentException("sources is null");
        }

        return new Default(sources);
    }

    public static Default from(String... sources) {

        if (sources == null) {
            throw new IllegalArgumentException("path is null");
        }

        return new Default(sources);
    }

    private Default(String[] sources) {
        this.sources = sources;
        this.parser.setKind(ASTParser.K_COMPILATION_UNIT);
        options.put(JavaCore.COMPILER_COMPLIANCE,JavaCore.VERSION_1_8);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM,JavaCore.VERSION_1_8);
        options.put(JavaCore.COMPILER_SOURCE,JavaCore.VERSION_1_8);
//        this.parser.setCompilerOptions(JavaCore.getOptions());
    }

    public void updateUnitName(Path fileJava) {
        try {
            byte[] readAllBytes = Files.readAllBytes(fileJava);
            String str = new String(readAllBytes);
            this.parser.setBindingsRecovery(true);
            this.parser.setResolveBindings(true);
            this.parser.setUnitName(fileJava.getFileName().toString());
            this.parser.setEnvironment(classpath,sources,new String[]{"UTF-8"},true);
            this.parser.setSource(str.toCharArray());
        } catch (IOException ex) {
            Logger.getLogger(Default.class.getName()).log(Level.SEVERE,null,ex);
        }
    }

    public void acceptVisitor(ASTVisitor visitor) {
        this.parser.setCompilerOptions(options);
        ASTNode createAST = parser.createAST(null);
        if (createAST.getAST().hasBindingsRecovery()) {
            Logger.getLogger(Default.class.getName()).log(Level.INFO,"Binding activated.");
        }
        createAST.accept(visitor);
    }
}
