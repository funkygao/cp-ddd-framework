package io.github.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

public class InboundOrder {
    @Autowired
    private ApplicationContext applicationContext;

    public String beans() {
        return Arrays.asList(applicationContext.getBeanDefinitionNames()).toString();
    }
}
