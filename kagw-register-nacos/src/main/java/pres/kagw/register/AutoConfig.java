package pres.kagw.register;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * krpc
 * 2022/8/18 17:54
 *
 * @author wangsicheng
 * @since
 **/
@Configuration
@Slf4j
@ConditionalOnProperty(name = "kagw.registeredPath", matchIfMissing = false)
public class AutoConfig {
    @Bean
    public NacosClient init() {
        log.info("NacosClient Start Init");
        return new NacosClient();
    }

}
