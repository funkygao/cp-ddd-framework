package org.example.cp.oms.plugin;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"io.github.dddplus", "org.example.cp"})
public class PluginConfig {
}
