package animals.service;

import animals.model.Node;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;

public class DatabaseService {

    public void save(Node root, String type, String fileName) {
        ObjectMapper objectMapper = getObjectMapper(type);
        try {
            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File(fileName), root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Node load(String type, String fileName) {
        Node root = new Node();
        ObjectMapper objectMapper = getObjectMapper(type);
        try {
            root = objectMapper.readValue(new File(fileName), Node.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;

    }

    private ObjectMapper getObjectMapper(String type) {
        ObjectMapper objectMapper;
        if (type.equals("xml")) {
            objectMapper = new XmlMapper();
        } else if (type.equals("yaml")) {
            objectMapper = new YAMLMapper();
        } else {
            objectMapper = new JsonMapper();
        }
        return objectMapper;
    }

    public boolean checkFile(String fileName) {
        File file = new File(fileName);
        return file.exists() && file.length() > 0;
    }
}
