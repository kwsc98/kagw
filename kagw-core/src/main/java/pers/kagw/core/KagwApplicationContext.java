package pers.kagw.core;


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

    public KagwApplicationContext(RegistryService registryService, int port) {
        try {
            this.nettyService = new NettyService(port);
            this.registryService = registryService;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }


}
