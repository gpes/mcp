package ifpb.gpes.graph;

import ifpb.gpes.Call;
import ifpb.gpes.Parse;
import ifpb.gpes.Project;
import ifpb.gpes.jdt.ParseStrategies;
import java.io.IOException;
import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author juan
 */
public class MatrixConfidenceTest {

    private final List<Call> calls = ofProject();
    private final SmartDirectGraph dg = new SmartDirectGraph();
    private static final String sources = "../mcp-samples/src/main/java/";

    private List<Call> ofProject() {
        Project project = Project
                .root("")
                .path(sources + "ifpb/gpes/domain/LambdaWithArguments.java")
                .sources(sources)
                .filter(".java");

        return Parse.with(ParseStrategies.JDT).from(project);
    }

    @Before
    public void before() throws IOException {
        calls.stream().forEach(dg::adicionarNos);
    }

    @Test
    public void VertexNumberTest() throws IOException, InterruptedException, ClassNotFoundException {
        Assert.assertEquals(17, dg.getGraph().vertexSet().size());
    }
//

    @Test
    public void EdgeNumberTest() throws IOException {
        Assert.assertEquals(14, dg.getGraph().edgeSet().size());
    }

    @Test
    public void WeightSumTest() throws IOException {
        assertNotEquals(0, dg.generateMatrix().weightSum());
        assertEquals(17, dg.generateMatrix().weightSum());
        Assert.assertEquals(10, dg.generateMatrix().matrizDeAdjacencia().toArray().length);
    }

}