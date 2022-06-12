package com.yilun.gl.dof.excute.framework.config.dynconfig.core.entity;

/**
 * @author gule
 * @date 2020.10.28
 */
public enum DyncConfigTypeEnum {
    /**
     * 默认的zk集群
     */
    ZOOKEEPER_00(0, "配置中心默认zk集群A", "zookeeper"),

    /**
     * 另外一套zk集群，暂时没有
     */
    ZOOKEEPER_01(1, "配置zk集群B", "zookeeperAnother"),
    /**
     * etcd
     */
    ETCD_00(10, "配置中心是etcd集群A", "default");

    private int index;
    private String displayName;
    private String configAddressPrefix;

    private DyncConfigTypeEnum(int index, String displayName, String configAddressPrefix) {
        this.index = index;
        this.displayName = displayName;
        this.configAddressPrefix = configAddressPrefix;
    }

    public int value() {
        return this.index;
    }

    public String displayName() {
        return displayName;
    }

    public String configAddressPrefix() {
        return configAddressPrefix;
    }
}
