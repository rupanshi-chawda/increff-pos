package com.increff.fop.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@ComponentScan("com.increff.fop")
@PropertySources({@PropertySource(value = "file:./fop.properties", ignoreResourceNotFound = true)})
public class SpringConfig {
}