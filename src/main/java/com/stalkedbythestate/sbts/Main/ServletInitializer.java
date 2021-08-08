package com.stalkedbythestate.sbts.Main;

// Copyright (c) 2021 Kim Hendrikse

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SbtsWebApplication.class);
    }

}
