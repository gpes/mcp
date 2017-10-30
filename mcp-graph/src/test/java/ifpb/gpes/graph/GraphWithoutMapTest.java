/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifpb.gpes.graph;

import ifpb.gpes.Call;
import ifpb.gpes.Parse;
import ifpb.gpes.Project;
import ifpb.gpes.jdt.ParseStrategies;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author juan
 */
public class GraphWithoutMapTest {

    private static final String sources = "../mcp-samples/src/main/java/";
    private List<Call> calls = ofProject();
    private SmartDirectGraph graph = new SmartDirectGraph();
    
    private List<Call> ofProject() {
        Project project = Project
                .root("")
                .path(sources + "ifpb/gpes/domain/AnonymousClass.java")
                .sources(sources)
                .filter(".java");

        return Parse.with(ParseStrategies.JDT).from(project);
    }

    @Before
    public void before(){
        calls.forEach((call) -> graph.adicionarNos(call));
    }
    
    @Test
    public void withMap() {
        calls.forEach((c)->System.out.println(c.callGraph()));
        System.out.println(graph.generateMatrix().toStr());
        graph.getGraph().edgeSet().stream().forEach(System.out::println);
    }

    @Test
    public void withoutMap() {

    }
}
