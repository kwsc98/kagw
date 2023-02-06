package pres.kagw.spring;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * kagw
 * 2022/8/18 17:51
 *
 * @author wangsicheng
 * @since
 **/
@ConfigurationProperties(prefix = "kagw")
@Data
public class KagwProperties {

    private String registeredPath;

    private int port = 8080;

}
