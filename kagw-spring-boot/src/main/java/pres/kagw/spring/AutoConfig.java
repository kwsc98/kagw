package pres.kagw.spring;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import pers.kagw.core.KagwApplicationContext;
import pers.kagw.core.KagwBuilderFactory;
import pers.kagw.core.registry.RegistryBuilderFactory;
import pers.kagw.core.registry.RegistryClientInfo;


/**
 * krpc
 * 2022/8/18 17:54
 *
 * @author wangsicheng
 * @since
 **/
@Configuration
@ConditionalOnClass(KagwProperties.class)
@EnableConfigurationProperties(KagwProperties.class)
@Slf4j
@ConditionalOnProperty(name = "kagw.registeredPath", matchIfMissing = false)
public class AutoConfig {

    @Bean(name = "KagwApplicationContext")
    @ConditionalOnMissingBean
    public KagwApplicationContext init(KagwProperties kagwProperties) {
        log.info("Kagw Start Init");
        return KagwBuilderFactory.builder()
                .setRegistryBuilderFactory(RegistryBuilderFactory.builder().setRegistryClientInfo(RegistryClientInfo.build(kagwProperties.getRegisteredPath())))
                .setPort(kagwProperties.getPort()).build();
    }

    @Bean
    @DependsOn({"krpcApplicationContext"})
    @ConditionalOnMissingBean
    public KagwPostProcessor dtpPostProcessor(@Qualifier("KagwApplicationContext") KagwApplicationContext kagwApplicationContext) {
        return new KagwPostProcessor(kagwApplicationContext);
    }


}
