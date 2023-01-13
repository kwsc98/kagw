package pers.kagw.core.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kwsc98
 */
public class HandlerService {
    private final Map<String, ComponentHandler<Object,Object>> channelMap = new HashMap<>();

    public void registerChannelHandler(String name, ComponentHandler<Object,Object> componentHandler) {
        channelMap.put(name, componentHandler);
    }

    public ComponentHandler<Object,Object> getComponentHandler(String name) {
        return channelMap.get(name);
    }


    public static void main(String[] args) {

    }

}
