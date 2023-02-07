package pers.kagw.core.registry;


import pers.kagw.core.dto.GroupDTO;

import java.io.FileNotFoundException;
import java.util.List;

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

    /**
     * 网关配置文件刷新
     **/
    void doRefresh(List<GroupDTO> list);


}
