package pers.kagw.core.channel;


import pers.kagw.core.handler.ComponentHandler;

/**
 * @author kwsc98
 */
public interface ChannelPipeline {


    NodeIterator getIterator();

    ChannelPipeline addFirst(ComponentNode componentNode);

    ChannelPipeline addLast(ComponentNode componentNode);

    ChannelPipeline addFirst(ComponentHandler handler, String configJsonStr);

    ChannelPipeline addLast(ComponentHandler handler, String configJsonStr);

}
