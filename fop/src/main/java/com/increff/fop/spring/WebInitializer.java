package com.increff.fop.spring;


import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

// hook for spring initialization and defines the starting point for spring configuration i.e. SpringConfig.class

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { SpringConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

}