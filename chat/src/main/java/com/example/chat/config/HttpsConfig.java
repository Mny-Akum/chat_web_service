package com.example.chat.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpsConfig {
    @Value("${server.port}")
    private int httpsPort;
    @Value("${http.port}")
    private int httpPort;
    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
//        {
//            @Override
//            protected void postProcessContext(Context context) {
//                // 设置安全约束
//                SecurityConstraint securityConstraint = new SecurityConstraint();
//                securityConstraint.setUserConstraint("CONFIDENTIAL");
//                // 创建安全集合
//                SecurityCollection collection = new SecurityCollection();
//                collection.addPattern("/*");
//                // 添加安全集合到安全约束
//                securityConstraint.addCollection(collection);
//                // 添加安全约束到上下文
//                context.addConstraint(securityConstraint);
//            }
//        };

        // 添加HTTP连接器
        tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
        return tomcat;
    }

    // 初始化HTTP连接器
    private Connector initiateHttpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(httpPort);
        connector.setSecure(false);
//        connector.setRedirectPort(httpsPort);
        return connector;
    }

}

