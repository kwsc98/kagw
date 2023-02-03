package pers.kagw.core.registry.impl;


import com.alibaba.nacos.common.utils.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.yaml.snakeyaml.Yaml;
import pers.kagw.core.common.JsonUtils;
import pers.kagw.core.common.utils.FileUtil;
import pers.kagw.core.dto.GroupDTO;
import pers.kagw.core.exception.ApiGateWayException;
import pers.kagw.core.handler.ChannelService;
import pers.kagw.core.registry.RegistryClient;
import pers.kagw.core.registry.RegistryClientInfo;

import java.io.*;
import java.util.List;
import java.util.Map;


/**
 * @author kwsc98
 */
@Slf4j
public class ConfigurationClient extends FileAlterationListenerAdaptor implements RegistryClient {


    private FileAlterationMonitor monitor;


    private final ChannelService channelService;

    private String configFileName = "";


    public ConfigurationClient(ChannelService channelService) {
        this.channelService = channelService;
    }


    @Override
    public void init(RegistryClientInfo registryClientInfo) {
        log.info("ConfigurationClient Init Start");
        String filePath = registryClientInfo.getServerAddr();
        String[] pathArray = filePath.split("/");
        configFileName = pathArray[pathArray.length - 1];
        File file = new File(filePath);
        doRefresh(file);
        StringBuilder stringBuilder = new StringBuilder("/");
        for (int i = 0; i < pathArray.length - 1; i++) {
            stringBuilder.append(pathArray[i]).append("/");
        }
        monitor(stringBuilder.toString());
        log.info("ConfigurationClient Init Done");
    }

    private void doRefresh(File file) {
        try {
            Map<String, Object> map = new Yaml().load(new FileReader(file));
            Object group = map.get("group");
            String json = JsonUtils.writeValueAsString(group);
            List<GroupDTO> list = JsonUtils.readValue(json, new TypeReference<List<GroupDTO>>() {
            });
            channelService.registrationGroupList(list);
        } catch (Exception e) {
            log.error("registrationGroupList Error : {}", e.toString(), e);
            throw new RuntimeException();
        }
    }

    public void monitor(String filePath) {
        try {
            FileAlterationObserver observer = new FileAlterationObserver(new File(filePath));
            observer.addListener(this);
            monitor = new FileAlterationMonitor(1000, observer);
            monitor.start();
        } catch (Exception e) {
            log.error("ConfigurationClient Monitor Error");
            throw new ApiGateWayException();
        }
    }

    /**
     * File changed Event.
     *
     * @param file The file changed (ignored)
     */
    @Override
    public void onFileChange(final File file) {
        if (!file.isFile() || !file.getName().equals(configFileName)) {
            log.info("ConfigurationClient Monitor Change But Not ConfigFile");
            return;
        }
        log.info("ConfigurationClient Monitor Change Start");
        doRefresh(file);
        log.info("ConfigurationClient Monitor Change Done");
    }


}
