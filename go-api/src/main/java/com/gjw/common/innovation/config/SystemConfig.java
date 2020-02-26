package com.gjw.common.innovation.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * @author gaojunwei
 * @date 2019/10/14 18:34
 */
@Configuration
public class SystemConfig {
    /**
     * 跨域白名单配置
     *
     * @return
     */
    @Bean("allowedOrigins")
    public Set<String> initOriginsSet() {
        Set<String> allowedOrigins = new HashSet<>();
        allowedOrigins.add("http://plc.jd.com");
        allowedOrigins.add("https://plc.jd.com");

        allowedOrigins.add("http://nbr.jd.com");
        allowedOrigins.add("https://nbr.jd.com");

        allowedOrigins.add("http://wu.jd.com");
        allowedOrigins.add("https://wu.jd.com");

        allowedOrigins.add("http://test.jdr.jd.com");
        allowedOrigins.add("https://test.jdr.jd.com");

        allowedOrigins.add("http://pb.jd.com");
        allowedOrigins.add("https://pb.jd.com");

        return allowedOrigins;
    }

    /**
     * 配置http某个端口自动跳转https
     * @return
     */
    @Bean
    public TomcatServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
        return tomcat;
    }

    private Connector initiateHttpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        //监听的http端口
        connector.setPort(8005);
        connector.setSecure(false);
        //跳转的https端口
        connector.setRedirectPort(8085);
        return connector;
    }
}