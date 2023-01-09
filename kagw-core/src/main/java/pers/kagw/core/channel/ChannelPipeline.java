package pers.kagw.core.channel;


/**
 * @author kwsc98
 */
public interface ChannelPipeline {

    ChannelPipeline addFirst(ComponentNode componentNode);

    ChannelPipeline addLast(ComponentNode componentNode);

    ChannelPipeline addFirst(ComponentHandler handler, String configJsonStr);

    ChannelPipeline addLast(ComponentHandler handler, String configJsonStr);

}
