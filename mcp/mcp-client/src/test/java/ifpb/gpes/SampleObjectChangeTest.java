package ifpb.gpes;

import br.edu.ifpb.gpes.export.ExportStrategy;
import ifpb.gpes.jdt.ParseStrategies;
import ifpb.gpes.study.Study;
import org.junit.Test;

public class SampleObjectChangeTest {

    @Test
    public void calls() {
        Project project = Project.root("")
                .path("../mcp-samples/src/main/java/ifpb/gpes/domain/SampleObject.java")
                .sources("../mcp-samples/src/main/java/")
                .filter(".java");
        try {
            Study.of(project)
                    .with(Parse.with(ParseStrategies.JDT))
                    .analysis(ExportStrategy.valueOf("CHANGEFACTOR").exportFactory("test"))
                    .execute();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
