package ifpb.gpes.jdt.main;

import ifpb.gpes.jdt.ListASTVisitor;
import ifpb.gpes.jdt.MyAnotherVisitor;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 09/05/2017, 10:55:34
 */
public class ListagemDasChamadasDosMetodos {

    private static final String DIR_SRC = "/home/juan/Downloads/parse-review/parse-jdt/src/main/java/ifpb/gpes/jdt/samples";
//    private static final String DIR_SRC = "/Users/job/Downloads/multi_bds/atividade/src";
    private static final String DIR_PATH = System.getProperty("java.home") + "/lib/rt.jar";

    public static void main(String[] args) {
        ASTParser parser = ASTParser.newParser(AST.JLS8);
//        TODO: Esses métodos podem/devem ser transferidos/usados em outra classe
//        readFilePath(new File(DIR_SRC), parser);
//        readFilesInPath(new File(DIR_SRC), parser);
//        walkFileTreeInPath(Paths.get(DIR_SRC), parser);
        previousMethod();
    }

    private static void walkFileTreeInPath(Path get, ASTParser parser) {
        try {
            Files.walkFileTree(get, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    showMethods(file, parser);
                    return super.visitFile(file, attrs);
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(ListagemDasChamadasDosMetodos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void readFilesInPath(Path get, ASTParser parser) {
        try {
            Files.list(get).forEach((t) -> {
                if (Files.isDirectory(t)) {
                    readFilesInPath(t, parser);
                } else {
                    showMethods(t, parser);
                }
            });
        } catch (IOException ex) {
        }
    }

//    public static void readFilePath1(File file, ASTParser parser) {
//        if (file.isDirectory()) {
//            for (File listFile : file.listFiles()) {
//                readFilePath(listFile, parser);
//            }
//        } else {
//            showMethods(file, parser);
//        }
//    }
    private static void previousMethod() {
        try {
            String file = "/Users/job/Documents/dev/gpes/parse-review/parse-jdt/src/main/java/ifpb/gpes/jdt/samples/D.java";
            
            ListASTVisitor visitor = new ListASTVisitor();
            
            byte[] readAllBytes = Files.readAllBytes(Paths.get(file));
            String str = new String(readAllBytes);
            
            ASTParser parser = ASTParser.newParser(AST.JLS8);
            parser.setResolveBindings(true);
            parser.setKind(ASTParser.K_COMPILATION_UNIT);
            
            parser.setBindingsRecovery(true);
            
            Map options = JavaCore.getOptions();
            parser.setCompilerOptions(options);
            
            String unitName = "D.java";
            parser.setUnitName(unitName);
            
            String[] sources = {"/Users/job/Documents/dev/gpes/parse-review/parse-jdt/src/main/java/"};
            String[] classpath = {System.getProperty("java.home") + "/lib/rt.jar"};
            parser.setEnvironment(classpath, sources, new String[]{"UTF-8"}, true);
            parser.setSource(str.toCharArray());
            CompilationUnit cut = (CompilationUnit) parser.createAST(null);
            if (cut.getAST().hasBindingsRecovery()) {
                System.out.println("Binding activated.");
            }
            cut.accept(visitor);
        } catch (IOException ex) {
            Logger.getLogger(ListagemDasChamadasDosMetodos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void showMethods(Path path, ASTParser parser) {

        try {
            String fileName = path.toFile().getName();
            setConfig(fileName, parser);

            byte[] readBytes = Files.readAllBytes(path);
            String str = new String(readBytes);

            parser.setSource(str.toCharArray());

            CompilationUnit cu = (CompilationUnit) parser.createAST(null);

            if (cu.getAST().hasBindingsRecovery()) {
                System.out.println("\nBinding activated.");
            }

//            System.out.println("_" + fileName + "_\n");
//            MyAnotherVisitor visitor = new MyAnotherVisitor(fileName);
            ListASTVisitor visitor = new ListASTVisitor();
            cu.accept(visitor);
//            visitor.showLista();

        } catch (IOException ex) {
            Logger.getLogger(ListagemDasChamadasDosMetodos.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setConfig(String name, ASTParser parser) {
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);

        Map options = JavaCore.getOptions();
        parser.setCompilerOptions(options);

        parser.setUnitName(name);

        String[] sources = {DIR_SRC};
        String[] classpath = {DIR_PATH};

        parser.setEnvironment(classpath, sources, new String[]{"UTF-8"}, true);
    }

}
