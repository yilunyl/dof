package com.yilun.gl.dof.excute.framework.other.config.dynconfig.impl.zk.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author gule
 * @date 2020/10/27 14:46
 * @Description:
 */
@Component
@ConfigurationProperties(prefix = "zookeeper")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DyncZookeeperProperties {
    private String address;
}
