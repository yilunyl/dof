package com.yilun.gl.dof.excute.framework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Redick01
 */
@SpringBootApplication(
        exclude = {
                DataSourceAutoConfiguration.class,
                KafkaAutoConfiguration.class,
        })
@EnableAspectJAutoProxy
@EnableAsync
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }
}
