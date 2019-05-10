package ifpb.gpes.jcf.io;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;
import java.util.stream.Collectors;

public class JsonFile {

    private InputStream inputStream;

    public JsonFile(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public JsonObject toJsonObject() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String jsonOfFile = reader.lines().collect(Collectors.joining());
        JsonReader jsonReader = Json.createReader(new StringReader(jsonOfFile));
        return jsonReader.readObject();
    }
}
