package pers.kagw.core;


import lombok.Getter;
import pers.kagw.core.dto.GroupDTO;
import pers.kagw.core.handler.ChannelService;
import pers.kagw.core.handler.HandlerService;
import pers.kagw.core.protocol.netty.NettyService;
import pers.kagw.core.registry.RegistryBuilderFactory;
import pers.kagw.core.registry.RegistryService;

/**
 * KagwApplicationContext
 * 2022/7/25 15:31
 *
 * @author wangsicheng
 **/
@Getter
public class KagwApplicationContext {

    private final NettyService nettyService;

    private final HandlerService handlerService;

    private final ChannelService channelService;

    private final DisposeService disposeService;

    private final RegistryBuilderFactory registryBuilderFactory;

    private RegistryService registryService;


    public KagwApplicationContext(RegistryBuilderFactory registryBuilderFactory, int port) {
        try {
            this.nettyService = new NettyService(port, this);
            this.handlerService = new HandlerService();
            this.channelService = new ChannelService(this);
            this.disposeService = new DisposeService(this);
            this.registryBuilderFactory = registryBuilderFactory;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void init() {
        this.registryService = registryBuilderFactory.init();
    }


}
