# kagw

kagw是一个基于netty为网络模型的高性能网关，支持API接口分组继承，自定义组件，高灵活网关（可以自定义路由组件，Mock组件，甚至可以直接当成Web框架使用）
<br/>

## SpringBoot的方式启动
### 支持已本地配置文件 or Nacos配置，基于yaml格式可实现热配置部署
引入 spring-boot-starter-kagw 包后只需增加配置资源地址 kagw.registeredPath 即可启动
 application.properties
 ```java
kagw.registeredPath = configuration:///.../kagw/kagw-example/src/main/resources/kagwConfig.yaml
kagw.registeredPath = nacos://114.116.3.221:8848
kagw.port=8081
 ``` 
### 配置文件
kagwConfig.yaml
<br/>
 ```java
group:
  - resourceName: TestResource #分组资源名
    resourceUrl: /kapi/test #分组前缀Url
    timeOut: 3000 #分组默认超时时间
    handlerList: #分组统一组件名
      - CommonRequestComponent
      - CommonResponseComponent
    routeList: #路由地址 weight为权重
      - addres: http://127.0.0.1:8083
        weight: 1
      - addres: http://127.0.0.2:8084
        weight: 0
    interfaceDTOList: #API接口
      - resourceName: GetTokenInterface #API接口名
        resourceUrl: /kapi/test/getToken #API接口Url
        groupExtends: true #是否继承分组组件
        handlerList: #接口组件
          - TokenBaseComponent:20 # {组件的资源名} : {该接口的组件配置}，例如该组件为获取token组件，而此接口获取token的有效期为20s
      - resourceName: GetNowTime
        resourceUrl: /kapi/test/getNowTime  #API接口Url
        routeResourceUrl: /krpc/test/getGetNowTime #API接口路由Url
        groupExtends: true
        handlerList:
          - CheckTokenComponent
      - resourceName: GetNowTimeV2
        resourceUrl: /kapi/test/getNowTimeV2
        routeResourceUrl: /krpc/test/getGetNowTime
        groupExtends: true
        handlerList:
          - CheckTokenComponent
          - MockBaseComponent:Mock Data
 ``` 

### 组件实现
自定义组件大致分为三种组件类型，请求处理组件，响应处理组件，基础组件（例如Mock组件，路由组件，获取Token组件等）
 ```java
 //组件顶层抽象类
public abstract class ComponentHandler<T, C> {

    public abstract Object handle(T object, C config);

    public C init(String configStr) {
        return null;
    }

}
//基础组件抽象类
public abstract class BaseComponentHandler<T,C> extends ComponentHandler<T,C> {}

//请求处理组件抽象类
public abstract class RequestComponentHandler<T,C> extends ComponentHandler<T,C> {}

//响应处理组件抽象类
public abstract class ResponseComponentHandler<T,C> extends ComponentHandler<T,C> {}

 ``` 
实现自定义组件只需要继承相应的抽象类即可，例如实现一个自定义获取token组件，实现之后只需要将beanName放入对应组件资源配置中，即可生效。<br/>
更多使用方法请参考 kagw-example
 ```java
/**
 * @author kwsc98
 */
@Component("TokenBaseComponent")
@Slf4j
public class TokenBaseComponent extends BaseComponentHandler<RequestHandlerDTO<CommonRequest>, Long> {

    @Resource
    private TokenService tokenService;

    @Override
    public Object handle(RequestHandlerDTO<CommonRequest> requestHandlerDTO, Long timeOut) {
        log.debug("TokenBaseComponent Handler Start");
        try {
            CommonRequest commonRequest = requestHandlerDTO.getContent();
            String key = commonRequest.getUserId() + ":" + commonRequest.getMerchantNo();
            String token = tokenService.setToken(key, timeOut);
            log.debug("TokenBaseComponent Handler Start");
            return token;
        } catch (Exception e) {
            log.error("Get Token Error : {}]", e.toString());
            throw new ApiGateWayException(ErrorEnum.TOKEN_ERROR);
        }
    }

    public Long init(String configStr) {
        if (StringUtils.isNotEmpty(configStr)) {
            return Long.valueOf(configStr);
        }
        return 60L;
    }

}

 ``` 
