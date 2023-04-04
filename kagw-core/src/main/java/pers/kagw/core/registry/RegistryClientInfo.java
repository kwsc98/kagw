package pers.kagw.core.registry;


import lombok.Data;
import pers.kagw.core.registry.impl.ConfigurationClient;

import java.util.Arrays;

/**
 * kagw注册中心参数
 * 2022/7/28 17:09
 *
 * @author wangsicheng
 **/
@Data
public class RegistryClientInfo {

    private String serverAddr;


    private RegistryClient registryClient;

    public static RegistryClientInfo build(String serverAddr) {
        return new RegistryClientInfo().setServerAddr(serverAddr).setRegistryClient(new ConfigurationClient());
    }

    private RegistryClientInfo setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
        return this;
    }

    public RegistryClientInfo setRegistryClient(RegistryClient registryClient) {
        this.registryClient = registryClient;
        return this;
    }
}
