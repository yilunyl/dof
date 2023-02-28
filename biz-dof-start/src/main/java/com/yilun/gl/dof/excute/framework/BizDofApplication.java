package com.yilun.gl.dof.excute.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author gl
 */
@SpringBootApplication(
    exclude = {
      DataSourceAutoConfiguration.class,
      KafkaAutoConfiguration.class,
    })
@EnableAspectJAutoProxy
@EnableAsync
public class BizDofApplication {
  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(BizDofApplication.class, args);
    String dsn = context.getEnvironment().getProperty("sentry.dsn");
  }
}
