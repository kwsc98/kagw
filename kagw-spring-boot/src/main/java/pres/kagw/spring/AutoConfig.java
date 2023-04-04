package pres.kagw.spring;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
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
@ConditionalOnClass(SpringKagwProperties.class)
@EnableConfigurationProperties(SpringKagwProperties.class)
@Slf4j
@ConditionalOnProperty(name = "kagw.registeredPath", matchIfMissing = false)
public class AutoConfig implements ApplicationRunner {

    private KagwApplicationContext kagwApplicationContext;

    @Bean(name = "KagwApplicationContext")
    @ConditionalOnMissingBean
    public KagwApplicationContext init(SpringKagwProperties kagwProperties) {
        log.info("Kagw Init");
        this.kagwApplicationContext = KagwBuilderFactory.builder()
                .setRegistryBuilderFactory(
                        RegistryBuilderFactory.builder()
                                .setRegistryClientInfo(
                                        RegistryClientInfo.build(kagwProperties.getRegisteredPath())
                                )
                )
                .setPort(kagwProperties.getPort())
                .build();
        return this.kagwApplicationContext;
    }

    @Bean
    @DependsOn({"KagwApplicationContext"})
    @ConditionalOnMissingBean
    public KagwPostProcessor dtpPostProcessor(@Qualifier("KagwApplicationContext") KagwApplicationContext kagwApplicationContext) {
        return new KagwPostProcessor(kagwApplicationContext);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Kagw Start");
        this.kagwApplicationContext.init();
    }
}
