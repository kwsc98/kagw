package pers.kagw.core;


import lombok.Getter;
import pers.kagw.core.handler.HandlerService;
import pers.kagw.core.protocol.netty.NettyService;
import pers.kagw.core.registry.RegistryService;

/**
 * KagwApplicationContext
 * 2022/7/25 15:31
 *
 * @author wangsicheng
 **/
public class KagwApplicationContext {

    private final RegistryService registryService;

    private final NettyService nettyService;

    @Getter
    private  InterfaceService interfaceService;

    @Getter
    private HandlerService handlerService;

    public KagwApplicationContext(RegistryService registryService, int port) {
        try {
            this.nettyService = new NettyService(port,this);
            this.registryService = registryService;
            this.interfaceService = new InterfaceService(this);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }


}
