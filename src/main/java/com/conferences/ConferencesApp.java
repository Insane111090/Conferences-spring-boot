package com.conferences;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ConferencesApp {
    private int maxUploadSizeInMb = 10 * 1024 * 1024;//10 MB
    public static void main(String[] args) {
        SpringApplication.run(ConferencesApp.class,
                              args
                             );
    }
    
    //Tomcat large file upload connection reset
    ////spring/spring-file-upload-and-connection-reset-issue/
    /*@Bean
    public Tomcat
    TomcatEmbeddedServletContainerFactory tomcatEmbedded() {
        
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        
        tomcat.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
            if ((connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>)) {
                //-1 means unlimited
                ((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(-1);
            }
        });
        
        return tomcat;*/
}
