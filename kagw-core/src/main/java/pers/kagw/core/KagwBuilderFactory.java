package pers.kagw.core;

import pers.kagw.core.registry.RegistryBuilderFactory;

/**
 * @author kwsc98
 */
public class KagwBuilderFactory {

    private RegistryBuilderFactory registryBuilderFactory;

    private int port;

    public static KagwBuilderFactory builder(){
        return new KagwBuilderFactory();
    }



    public KagwBuilderFactory setRegistryBuilderFactory(RegistryBuilderFactory registryBuilderFactory) {
        this.registryBuilderFactory = registryBuilderFactory;
        return this;
    }

    public KagwBuilderFactory setPort(int port) {
        this.port = port;
        return this;
    }

    public KagwApplicationContext build() {
        return new KagwApplicationContext(this.registryBuilderFactory,this.port);
    }

}
