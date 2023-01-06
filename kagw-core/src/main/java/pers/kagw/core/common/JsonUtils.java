package pers.kagw.core.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

/**
 * @author kwsc98
 */
public class JsonUtils {

    private static final JsonMapper JSON_MAPPER = new JsonMapper();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> T readValue(byte[] bytes, Class<T> clazz) throws Exception {
        return JSON_MAPPER.readValue(bytes, clazz);
    }

    public static <T> T readValue(String jsonStr, Class<T> clazz) throws Exception {
        return JSON_MAPPER.readValue(jsonStr, clazz);
    }

    public static byte[] writeValueAsBytes(Object object) throws Exception {
        return JSON_MAPPER.writeValueAsBytes(object);
    }

    public static String formatJsonStr(String jsonStr) throws JsonProcessingException {
        return JSON_MAPPER.writeValueAsString(JSON_MAPPER.readValue(jsonStr, Object.class));
    }

}
