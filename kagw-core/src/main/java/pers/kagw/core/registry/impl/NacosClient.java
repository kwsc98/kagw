package pers.kagw.core.registry.impl;


import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;
import pers.kagw.core.KagwApplicationContext;
import pers.kagw.core.common.JsonUtils;
import pers.kagw.core.dto.GroupDTO;
import pers.kagw.core.handler.ChannelService;
import pers.kagw.core.registry.RegistryClient;
import pers.kagw.core.registry.RegistryClientInfo;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * kagw
 * 2022/8/17 14:12
 *
 * @author wangsicheng
 **/
@Slf4j
public class NacosClient implements RegistryClient, Listener {


    @Override
    public void init(RegistryClientInfo registryClientInfo) {
        try {
            Properties properties = new Properties();
            properties.put("serverAddr", registryClientInfo.getServerAddr());
            ConfigService configService = NacosFactory.createConfigService(properties);
            String yamlStr = configService.getConfigAndSignListener("kagw", "kagw_group", 5000, this);
            Map<String, Object> map = new Yaml().load(yamlStr);
            Object group = map.get("group");
            String json = JsonUtils.writeValueAsString(group);
            List<GroupDTO> list = JsonUtils.readValue(json, new TypeReference<List<GroupDTO>>() {
            });
            for (GroupDTO groupDTO : list) {
                ChannelService.registrationGroup(groupDTO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Data
    public static class NacosYaml {
        List<GroupDTO> group;
    }

    @Override
    public Executor getExecutor() {
        return null;
    }

    @Override
    public void receiveConfigInfo(String configInfo) {

    }
}
