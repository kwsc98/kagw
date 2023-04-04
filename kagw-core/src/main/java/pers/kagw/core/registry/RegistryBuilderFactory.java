package pers.kagw.core.registry;


import pers.kagw.core.handler.ChannelService;
import pers.kagw.core.registry.impl.ConfigurationClient;

/**
 * kagw注册中心构建工厂
 * 2022/7/25 16:03
 *
 * @author lanhaifeng
 **/
public class RegistryBuilderFactory {

    private RegistryClientInfo registryClientInfo;

    public static RegistryBuilderFactory builder() {
        return new RegistryBuilderFactory();
    }


    public RegistryService init(ChannelService channelService) {
        RegistryService registryService = RegistryService.build(registryClientInfo.getRegistryClient());
        registryService.init(registryClientInfo, channelService);
        return registryService;
    }

    public RegistryBuilderFactory setRegistryClientInfo(RegistryClientInfo registryClientInfo) {
        this.registryClientInfo = registryClientInfo;
        return this;
    }

    public void setRegistryClient(RegistryClient registryClient){
        this.registryClientInfo.setRegistryClient(registryClient);
    }

}
