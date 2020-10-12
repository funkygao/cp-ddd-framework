package org.example.cp.oms.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"io.github.dddplus", "org.example.cp", "org.example.bp"})
public class AppConfig {
}
