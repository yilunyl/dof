package com.yilun.gl.dof.excute.framework.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @ClassName: biz-dof SimpleBean
 * @Description: com.yilun.gl.dof.excute.framework.starter
 * @Author: 逸伦
 * @Date: 2023/2/28 09:28
 * @Version: 1.0
 */
@EnableConfigurationProperties(SimpleBean.class)
@ConfigurationProperties(prefix = "simplebean")
public class SimpleBean {
}
