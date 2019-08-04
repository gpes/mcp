package br.edu.ifpb.gpes.script;

import br.edu.ifpb.gpes.export.ExportStrategy;
import com.fasterxml.jackson.databind.JsonNode;
import ifpb.gpes.Parse;
import ifpb.gpes.Project;
import ifpb.gpes.jcf.io.JsonFile;
import ifpb.gpes.jdt.ParseStrategies;
import ifpb.gpes.study.Study;

import java.io.InputStream;

public class TCCMetricLoader {

    public static void main(String[] args) {
        InputStream stream = TCCMetricLoader.class.getClassLoader().getResourceAsStream("projects.json");
        JsonFile file = new JsonFile(stream);
        JsonNode projects = file.toJsonObject();
        projects.forEach(node -> {
            Project project = Project.root(node.get("root").asText())
                    .path(node.get("source").asText())
                    .sources(node.get("source").asText())
                    .filter(".java");
            try {
                Study.of(project)
                        .with(Parse.with(ParseStrategies.JDT))
                        .analysis(ExportStrategy.valueOf("CHANGEFACTOR").exportFactory(node.get("path").asText()))
                        .execute();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
