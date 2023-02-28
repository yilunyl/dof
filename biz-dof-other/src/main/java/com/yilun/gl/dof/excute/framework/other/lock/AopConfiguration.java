package com.yilun.gl.dof.excute.framework.other.lock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: gl
 * @Date: 2020/9/2 13:25
 * @Description: 测试用
 */
@Configuration
public class AopConfiguration {
    @Bean
    public DofRedisLockAspect dofRedisLockAspect() {
        return new DofRedisLockAspect();
    }
}
