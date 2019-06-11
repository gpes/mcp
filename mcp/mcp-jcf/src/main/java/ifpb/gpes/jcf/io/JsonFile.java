package ifpb.gpes.jcf.io;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JsonFile {

    private InputStream inputStream;
    private JsonNode node;

    public JsonFile(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public JsonNode toJsonObject() {
        try {
            if(node == null)
                node = new ObjectMapper().readTree(inputStream);
            return node;
        } catch (IOException e) {
            return null;
        }
    }
}
