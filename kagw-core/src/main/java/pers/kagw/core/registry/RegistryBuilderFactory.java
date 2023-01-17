package pers.kagw.core.registry;


import pers.kagw.core.registry.impl.NacosClient;
import pers.kagw.core.registry.impl.ZookeeperClient;

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


    public RegistryService init() {
        RegistryService registryService = null;
        switch (registryClientInfo.getClient()) {
            case Nacos:
                registryService = RegistryService.build(new NacosClient());
                break;
            case Zookeeper:
                registryService = RegistryService.build(new ZookeeperClient());
                break;
            default:
                throw new RuntimeException();
        }
        registryService.init(registryClientInfo);
        return registryService;
    }

    public RegistryBuilderFactory setRegistryClientInfo(RegistryClientInfo registryClientInfo) {
        this.registryClientInfo = registryClientInfo;
        return this;
    }
}
