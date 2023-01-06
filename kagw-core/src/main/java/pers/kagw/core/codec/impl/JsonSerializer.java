package pers.kagw.core.codec.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import pers.kagw.core.codec.Serializer;
import pers.kagw.core.common.JsonUtils;

/**
 * @author kwsc98
 */
public class JsonSerializer implements Serializer {

    @Override
    public byte[] serialize(Object object) throws Exception {
        return JsonUtils.writeValueAsBytes(object);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        return JsonUtils.readValue(bytes, clazz);
    }
}
