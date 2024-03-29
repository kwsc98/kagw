package pers.kagw.core.registry;


import lombok.Setter;
import pers.kagw.core.handler.ChannelService;

/**
 * kagw注册中心处理Service
 * 2022/7/28 15:50
 *
 * @author wangsicheng
 **/
public class RegistryService  {

    @Setter
    private RegistryClient registryClient;

    public static RegistryService build(RegistryClient registryClient){
        RegistryService registryService = new RegistryService();
        registryService.registryClient = registryClient;
        return registryService;
    }

    public void init(RegistryClientInfo registryClientInfo, ChannelService channelService) {
        this.registryClient.init(registryClientInfo.getServerAddr(),channelService);
    }


}
