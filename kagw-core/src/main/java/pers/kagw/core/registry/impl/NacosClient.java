package pers.kagw.core.registry.impl;


import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import pers.kagw.core.registry.RegistryClient;
import pers.kagw.core.registry.RegistryClientInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * kagw
 * 2022/8/17 14:12
 *
 * @author wangsicheng
 **/
@Slf4j
public class NacosClient implements RegistryClient, EventListener {

    private NamingService namingService;

    @Override
    public void init(RegistryClientInfo registryClientInfo) {
        try {
            this.namingService = NamingFactory.createNamingService(registryClientInfo.getServerAddr());
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEvent(Event event) {

    }
}
