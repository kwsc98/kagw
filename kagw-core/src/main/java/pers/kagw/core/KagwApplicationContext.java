package pers.kagw.core;


import pers.kagw.core.registry.RegistryService;

/**
 * KagwApplicationContext
 * 2022/7/25 15:31
 *
 * @author wangsicheng
 **/
public class KagwApplicationContext {

    private final RegistryService registryService;

    public KagwApplicationContext(RegistryService registryService, String port) {
        try {
            this.registryService = registryService;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }


}
