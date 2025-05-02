package bookstore.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class JsonDataReader {
    public static <T> T readData(String resourcePath, Class<T> clazz) {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Resource not found: " + resourcePath);
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(inputStream, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON data from: " + resourcePath, e);
        }
    }

    public static List<Map<String, Object>> readInvalidData(String filePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(
                    new File("src/test/resources/" + filePath),
                    new TypeReference<>() {
                    });
        } catch (IOException e) {
            throw new RuntimeException("Failed to read invalid test data from " + filePath, e);
        }
    }

}
