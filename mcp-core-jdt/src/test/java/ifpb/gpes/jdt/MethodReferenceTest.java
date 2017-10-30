package ifpb.gpes.jdt;

import ifpb.gpes.Call;
import ifpb.gpes.Parse;
import ifpb.gpes.Project;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import org.junit.Test;

public class MethodReferenceTest {

    private final List<Call> result = ofMethodReferenceClass();
    private static final Logger logger = Logger.getLogger(MethodReferenceTest.class.getName());
    private static final String sources = "../mcp-samples/src/main/java/";

    @Test
    public void testeChamadaComReference() {
    }

    @Test
    public void testM1() {
        List<Call> expected = ofListM1();
        result.forEach(no -> System.out.println(no.callGraph()));
    }

    private List<Call> ofListM1() {
        return Arrays.asList();
    }

    private List<Call> ofMethodReferenceClass() {
        Project project = Project
                .root("")
                .path(sources + "ifpb/gpes/domain/MethodReferenceExample.java") // root
                .sources(sources) // root - não obrigatorio
                .filter(".java");

        return Parse.with(ParseStrategies.JDT).from(project);

    }

}
