package pers.kagw.core.registry;


import pers.kagw.core.InterfaceContextDetails;
import pers.kagw.core.InterfaceInfo;
import pers.kagw.core.role.Role;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * kagw注册中心接口
 * 2022/7/28 15:53
 *
 * @author wangsicheng
 **/
public interface RegistryClient {

    String ROOT_PATH = "kagwApplication";

    /**
     * 注册中心初始化方法
     **/
    void init(RegistryClientInfo registryClientInfo);


}
