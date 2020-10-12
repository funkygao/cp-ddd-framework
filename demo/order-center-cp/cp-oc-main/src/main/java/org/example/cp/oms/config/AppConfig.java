package org.example.cp.oms.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"org.cdf.ddd", "org.example.cp", "org.example.bp"})
public class AppConfig {
}
