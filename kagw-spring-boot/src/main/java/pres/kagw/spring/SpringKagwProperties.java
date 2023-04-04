package pres.kagw.spring;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import pers.kagw.core.dto.KagwProperties;

/**
 * kagw
 * 2022/8/18 17:51
 *
 * @author wangsicheng
 * @since
 **/
@ConfigurationProperties(prefix = "kagw")
public class SpringKagwProperties extends KagwProperties {

}
