# nacos集成
## maven坐标
```xml
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>

    <os.maven.plugin.version>1.7.1</os.maven.plugin.version>
    <com.google.protobuf.version>3.25.1</com.google.protobuf.version>
    <protobuf.maven.plugin.version>0.6.1</protobuf.maven.plugin.version>

    <spring-boot.version>3.0.2</spring-boot.version>
    <spring-cloud-alibaba.version>2022.0.0.0-RC2</spring-cloud-alibaba.version>
</properties>

 <dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>${spring-boot.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-alibaba-dependencies</artifactId>
            <version>${spring-cloud-alibaba.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```
## 配置文件
```yaml
spring:
  application:
    name: nacos-config-example
  cloud:
    nacos:
      config:
        username: nacos
        password: nacos
        server-addr: 127.0.0.1:8848
        namespace: dev
        enable-remote-sync-config: true
  config:
    import:
      - nacos:gjw-test.yml?refreshEnabled=true&group=dev-group
      - nacos:groovy_script?refreshEnabled=true&group=dev_group
```
## Nacos配置中心配置
![nacos配置文件](image/2.png)

## 动态更新配置值 @RefreshScope
```java
@Setter
@Getter
@Configuration
@RefreshScope
public class AppConfig {
    @Value("${gjw.test}")
    private String value;
}
```
## 注入ConfigService实例bean
- 注入ConfigService实例bean
```java
@Setter
@Getter
@Configuration
@RefreshScope
public class AppConfig {
    @Value("${gjw.test}")
    private String value;

    @Resource
    private NacosConfigManager nacosConfigManager;

    @Bean
    public ConfigService getConfigService() {
        return nacosConfigManager.getConfigService();
    }
}
```
- 用于获取nacos配置文件整体内容
```java
@Slf4j
@Component
public class TextListener {
    @Resource
    private ConfigService configService;

    public String getConfigContent(String dataId, String group) {
        try {
            return configService.getConfig(dataId, group, 5000);
        } catch (NacosException e) {
            log.error("获取配置失败", e);
            return null;
        }
    }
}
```
## 测试
```java
@RestController
@RequestMapping("/nacos")
public class NacosTestController {
    @Resource
    private AppConfig appConfig;
    @Resource
    private TextListener textListener;

    @GetMapping
    public String test() {
        return "hello nacos " + appConfig.getValue();
    }

    @Scheduled(cron = "0/2 * * * * ?")
    public void test2() {
        System.out.println("配置项 gjw.test -> " + appConfig.getValue());
        System.out.println("脚本内容 -> "+textListener.getConfigContent("groovy_script","dev_group"));
    }
}
```
- 效果  
![控制台打印](image/1.png)
