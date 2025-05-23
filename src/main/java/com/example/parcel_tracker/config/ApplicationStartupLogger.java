package com.example.parcel_tracker.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupLogger implements ApplicationListener<WebServerInitializedEvent> {

    private static final Logger log = LoggerFactory.getLogger(ApplicationStartupLogger.class);

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        int port = event.getWebServer().getPort();
        String contextPath = event.getApplicationContext().getEnvironment()
                                  .getProperty("server.servlet.context-path", "");

        if (!contextPath.isEmpty() && !contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }
        if (contextPath.endsWith("/") && contextPath.length() > 1) {
            contextPath = contextPath.substring(0, contextPath.length() - 1);
        }

        String baseUrl = String.format("http://localhost:%d%s", port, contextPath);

        log.warn("\n-------------------------------------------------------------------------------\n"
                +"Application is running! Access it at: {}\n"
                +"-------------------------------------------------------------------------------\n", baseUrl);
    }
}