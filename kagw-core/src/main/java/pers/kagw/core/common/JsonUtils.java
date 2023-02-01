package pers.kagw.core.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.util.List;

/**
 * @author kwsc98
 */
public class JsonUtils {

    private static final JsonMapper JSON_MAPPER = new JsonMapper();

    public static <T> T readValue(byte[] bytes, Class<T> clazz) throws Exception {
        return JSON_MAPPER.readValue(bytes, clazz);
    }

    public static <T> T readValue(String jsonStr, Class<T> clazz) throws Exception {
        return JSON_MAPPER.readValue(jsonStr, clazz);
    }

    public static <T> T readValue(String jsonStr, TypeReference<T> typeReference) throws Exception {
        return JSON_MAPPER.readValue(jsonStr, typeReference);
    }


    public static byte[] writeValueAsBytes(Object object) throws Exception {
        return JSON_MAPPER.writeValueAsBytes(object);
    }

    public static String formatJsonStr(String jsonStr) throws JsonProcessingException {
        return JSON_MAPPER.writeValueAsString(JSON_MAPPER.readValue(jsonStr, Object.class));
    }

    public static String writeValueAsString(Object object) throws JsonProcessingException {
        return JSON_MAPPER.writeValueAsString(object);
    }


}
