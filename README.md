# 自定义starter实现案例

### 制作镜像
```bash
docker build -t go-server:v1.0 .
```
## 实现自定义starter

>- 其中spring-boot-configuration-processor这个依赖主要用于IDEA支持和编译时生成元数据。
>- 自动配置类负责定义Spring Boot应用程序中的通用配置和功能。   
   这个类通常使用@Configuration注解进行标记，在这个类中注入服务、组件或其他你需要自动配置的对象。

### 使用到的依赖
```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-autoconfigure</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```


### 指定自动装配类
>- 在resources文件夹下创建一个META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports文件。
>- 文件的内容是：每个实现类的全类名单独一行。
>- 当应用启动时，SpringBoot的引导过程会扫描所有已引入jar包中的spring.factories文件，并根据EnableAutoConfiguration键下的类来加载和执行相应的自动配置逻辑。
>- 当然如果不使用这个配置，在调用方使用@ComponentScan也可以扫描到CoderAcademyAutoConfig。但是这跟Starter的设计理念相悖。在Starter的设计中，一般不推荐调用方手动进行额外的扫描。这是因为调用方引入了Starter，就应该依赖于 Starter提供的自动配置。手动扫描可能会导致不必要的麻烦，例如循环依赖、配置类的重复加载等问题。


```text
#指定自动配置类
com.go.starter.config.CoderAcademyPropertiesAutoConfig
com.go.starter.config.CoderAcademyAutoConfig
```

### 属性配置提示
- 我们在使用其他的官方Starter时在application.properties或者application.yml配置相关属性时，
IDEA会自动给出属性的Key的提示，以及给出默认值。那么在自定义Starter中该如何实现这功能呢？
其实这就需要用到了我们引入的spring-boot-configuration-processor依赖。

- spring-boot-configuration-processor 是 Spring Boot 提供的一个注解处理器，
用于处理 @ConfigurationProperties 注解，生成配置属性的元数据，以提供更好的 IDE 支持和配置文件提示。
注解处理器会扫描项目中标注了@ConfigurationProperties 注解的类，
然后生成包含有关这些配置属性的详细信息的 spring-configuration-metadata.json文件。该文件位于META-INF下。
这个元数据文件包含了配置属性的描述、类型、默认值等信息，以提供更好的代码提示和文档生成功能。
元数据文件被 IDE（如 IDEA、Eclipse）使用，用于提供更强大的代码提示和补全功能。
开发者在编辑配置文件时可以看到配置属性的描述、类型等信息，更容易正确地配置应用程序。

当然添加依赖之后，我们还需要添加Maven的插件（如果使用的是Maven）。
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.13.0</version>
            <configuration>
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-configuration-processor</artifactId>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```
![元数据文件](image/1.jpg)
![配置属性信息提示效果](image/2.jpg)

## 相关注解说明
### @EnableConfigurationProperties 说明
```text
@EnableConfigurationProperties 是 Spring Boot 中的一个注解，用于启用 @ConfigurationProperties 注解的类的自动配置。
[classes](spring-starter-demo%2Ftarget%2Fclasses)
@ConfigurationProperties 注解通常用于将外部配置文件中的属性绑定到 Java 对象中。
通过使用 @EnableConfigurationProperties，可以将这些配置属性类自动注册为 Spring Bean，并在应用程序中使用。

以下是使用 @EnableConfigurationProperties 的一般步骤：
1、创建一个配置属性类，使用 @ConfigurationProperties 注解来指定前缀和属性。
2、在配置类上使用 @EnableConfigurationProperties 注解来启用配置属性的自动配置。
3、在应用程序中，可以通过 @Autowired 或其他依赖注入方式注入配置属性类的实例，并访问其中的属性。
这样，就可以方便地将外部配置与应用程序的代码进行绑定，并在需要的地方使用这些配置属性。
```
### @PropertySource说明
```text
在SpringBoot应用中，通过application.properties或application.yml设置的属性具有较高的优先级。  
如果使用@PropertySource加载的属性与前者有冲突，则会被后者覆盖。
```
### @ConditionalOnMissingBean 说明
```text
@ConditionalOnMissingBean 是 Spring 框架中的一个条件注解，用于在特定条件下控制 Bean 的创建。
它的作用是：当容器中不存在指定类型的 Bean 时，才会创建当前注解修饰的 Bean。

利用@ConditionalOnMissingBean注解来确保仅在容器中尚无CoderAcademyService Bean时才进行创建操作。
这样就避免了重复注册同一类型Bean导致的问题。
```
### @ConditionalOnBean 说明
```text
@ConditionalOnBean 是 Spring 框架中的一个条件注解，
用于根据容器中是否存在特定的 Bean 来决定是否执行某个配置或创建某个 Bean。
它的作用是：只有当容器中存在指定类型或指定名称的 Bean 时，才会执行当前注解修饰的配置或创建当前注解修饰的 Bean。
```
### @AutoConfigureAfter 说明
```text
@AutoConfigureAfter 是 Spring Boot 中的一个注解，用于指定自动配置类的加载顺序。
它的作用是确保当前自动配置类在指定的其他自动配置类之后加载。通过设置 @AutoConfigureAfter，
可以控制自动配置类之间的依赖关系，确保某些配置在其他配置之前完成。

此外，Spring Boot 还提供了其他类似的注解，如 @AutoConfigureBefore 和 @AutoConfigureOrder，
用于更精细地控制自动配置类的加载顺序。
```