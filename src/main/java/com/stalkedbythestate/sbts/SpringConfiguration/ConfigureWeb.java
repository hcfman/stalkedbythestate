package com.stalkedbythestate.sbts.SpringConfiguration;

// Copyright (c) 2021 Kim Hendrikse

import org.apache.catalina.core.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Set;
import java.util.stream.Stream;

@Configuration
public class ConfigureWeb implements ServletContextInitializer {
    private static final Logger logger = LoggerFactory.getLogger(ConfigureWeb.class);


    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        registerServlets(servletContext);
   }

    private static Stream<String> getDeepResourcePaths(ServletContext servletContext, String path) {
        return (path.endsWith("/")) ? servletContext.getResourcePaths(path).stream().flatMap(p -> getDeepResourcePaths(servletContext, p))
                : Stream.of(path);
    }

    private void registerServlets(ServletContext servletContext) {
        Set<String> resourcePaths = servletContext.getResourcePaths("/jsp/");

        if (resourcePaths != null)
            getDeepResourcePaths(servletContext, "/jsp/").forEach(jspPath -> {
                ServletRegistration.Dynamic reg = servletContext.addServlet(jspPath, Constants.JSP_SERVLET_CLASS);
                reg.setInitParameter("jspFile", jspPath);
                reg.setLoadOnStartup(99);
                reg.addMapping(jspPath);
            });
    }
}
