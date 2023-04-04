package pres.kagw.spring;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import pers.kagw.core.KagwApplicationContext;
import pers.kagw.core.handler.ComponentHandler;
import pers.kagw.core.handler.HandlerService;
import pers.kagw.core.registry.RegistryClient;

/**
 * krpc
 * 2022/7/27 10:43
 *
 * @author wangsicheng
 * @since
 **/
public class KagwPostProcessor implements BeanPostProcessor {


    private final KagwApplicationContext krpcApplicationContext;


    KagwPostProcessor(KagwApplicationContext krpcApplicationContext) {
        this.krpcApplicationContext = krpcApplicationContext;
    }


    @Override
    public Object postProcessBeforeInitialization(@Nullable Object bean, @Nullable String beanName) throws BeansException {
        assert beanName != null;
        assert bean != null;
        HandlerService handlerService = krpcApplicationContext.getHandlerService();
        if (bean instanceof ComponentHandler) {
            handlerService.registerChannelHandler(beanName, (ComponentHandler<?, ?>) bean);
        }
        if (bean instanceof RegistryClient) {
            krpcApplicationContext.getRegistryBuilderFactory().setRegistryClient((RegistryClient) bean);
        }
        return bean;
    }
}
