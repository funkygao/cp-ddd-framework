package org.example.cp.oms;

import lombok.extern.slf4j.Slf4j;
import org.example.cp.oms.domain.CoreDomain;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class OrderCenterLauncher {

    public static void main(String[] args) throws Exception {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        context.start();

        CoreDomain.getApplicationLifeCycle().started();
        enableGracefulShutdown();

        CountDownLatch latch = new CountDownLatch(1);
        try {
            log.info("DooLauncher launched successfully!");

            latch.await();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }

    private static void enableGracefulShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // 由于log4j有自己的shutdown机制，此时它可能已经关闭了，因此，最把握的还是System.out
            System.out.println(getDateString() + " shutdown hook now...");

            CoreDomain.getApplicationLifeCycle().stop();

            // 等待业务线程执行完毕，给他们做收尾的机会：best effort
            try {
                System.out.println(getDateString() + " waiting 20s for biz threads cleanup...");
                Thread.sleep(20 * 1000);
            } catch (InterruptedException ignored) {
                log.error(ignored.getMessage(), ignored);
                Thread.currentThread().interrupt();
            }

            CoreDomain.getApplicationLifeCycle().stopped();

            // 此时log线程已经关闭了，只能打到控制台了
            System.out.println(getDateString() + " shutdown hook done.");
        }, "shutdownHook"));
    }

    private static String getDateString() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS"));
    }
}
