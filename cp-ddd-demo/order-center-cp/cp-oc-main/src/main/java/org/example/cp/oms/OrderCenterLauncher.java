package org.example.cp.oms;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.example.cp.oms.domain.CoreDomain;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.net.URL;
import java.security.ProtectionDomain;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class OrderCenterLauncher {
    private static final int DEFAULT_PORT = 9090;
    private static final String DEFAULT_CONTEXT_PATH = "/ddd-demo";

    public static void main(String[] args) {
        Server server = createJettyServer(DEFAULT_PORT, DEFAULT_CONTEXT_PATH);
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            log.error("Unexpected", e);
        } finally {
            try {
                server.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Server createJettyServer(int port, String contextPath) {
        Server server = new Server(port);
        server.setStopAtShutdown(true);

        ProtectionDomain protectionDomain = OrderCenterLauncher.class.getProtectionDomain();
        URL location = protectionDomain.getCodeSource().getLocation();
        String warFile = location.toExternalForm();

        WebAppContext context = new WebAppContext(warFile, contextPath);
        context.setServer(server);

        // 设置 work dir, war包将解压到该目录
        String currentDir = new File(location.getPath()).getParent();
        File workDir = new File(currentDir, "work");
        context.setTempDirectory(workDir);

        server.setHandler(context);
        return server;
    }

}
