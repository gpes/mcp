/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.gpes.graph;

import ifpb.gpes.Call;
import ifpb.gpes.Parse;
import ifpb.gpes.Project;
import ifpb.gpes.graph.io.MatrixExportManager;
import ifpb.gpes.jdt.ParseStrategies;
import java.nio.file.Paths;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author juan
 */
public class MatrixExportTest {

    private final List<Call> calls = ofProject();
    private final DefaultDirectGraph dg = new DefaultDirectGraph();
    private static final String sources = "../mcp-samples/src/main/java/";

    private List<Call> ofProject() {
        Project project = Project
                .root("")
                .path(sources + "ifpb/gpes/domain/LambdaWithArguments.java")
                .sources(sources)
                .filter(".java");

        return Parse.with(ParseStrategies.JDT).from(project);
    }
    
    @Test
    public void writingFile(){
        new MatrixExportManager().export(calls);
        Assert.assertEquals(Paths.get("../matrix.csv").toFile().exists(), true);
    }
}
