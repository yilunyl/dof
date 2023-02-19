package com.yilun.gl.dof.excute.framework.config.dynconfig.core;


import com.yilun.gl.dof.excute.framework.config.dynconfig.core.entity.DyncConfigTypeEnum;
import com.yilun.gl.dof.excute.framework.config.dynconfig.core.entity.ConfigChangedEvent;
import com.yilun.gl.dof.excute.framework.config.dynconfig.core.entity.RefreshConfgiEntity;
import com.yilun.gl.dof.excute.framework.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.Objects;

/**
 * @Auther: gl
 * @Date: 2020/10/28 11:00
 * @Description:
 */
public abstract class BasicDyncConfigService implements DyncConfigService, ApplicationEventPublisherAware, EnvironmentAware {

    private final static Logger log = LoggerFactory.getLogger(BasicDyncConfigService.class);
    protected ApplicationEventPublisher applicationEventPublisher;

    protected Environment environment;

    /**
     * 获取事件发布器
     *
     * @param applicationEventPublisher applicationEventPublisher
     */
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 获取环境信息
     *
     * @param environment environment
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * 发送配置节点变更消息
     *
     * @param refresh 事件信息
     */
    @Override
    public void refreshProperties(RefreshConfgiEntity refresh) {
        ConfigChangedEvent changeEvent = new ConfigChangedEvent(refresh);
        applicationEventPublisher.publishEvent(changeEvent);
    }

    @Override
    public byte[] createNodeCache(String path) {
        //初始化客户端，并开启
        this.createConfigClient();

        //第一次获取初始化数据
        byte[] dataBytes = this.initFirstData(path);
        if (Objects.nonNull(dataBytes)) {
            refreshProperties(RefreshConfgiEntity.builder().data(dataBytes).wholePath(path).build());
            log.info("DyncConfigService节点{}的初始化数据为：{}", path, new String(dataBytes));
        } else {
            log.error("DyncConfigService节点{}初始化数据为空...", path);
        }

        //注册监听器
        this.registerListener(path);

        //返回数据
        return dataBytes;

    }

    /**
     * 获取配置类型
     *
     * @return 0代码zk
     */
    @Override
    public DyncConfigTypeEnum getConfigType() {
        return this.getDyncConfigType();
    }

    /**
     * 销毁处理
     */
    @Override
    public void destroy() {
        this.doDestroy();
    }

    /**
     * 创建并初始化客户端
     */
    protected abstract void createConfigClient();

    /**
     * 第一次初始化数据
     */
    protected abstract byte[] initFirstData(String path);

    /**
     * 注册监听器
     */
    protected abstract void registerListener(String path);

    /**
     * 获取配置类型
     */
    protected abstract DyncConfigTypeEnum getDyncConfigType();

    /**
     * 销毁,主要用来已出注册给zk的监听器和关闭连接等操作，必须要实现
     */
    protected abstract void doDestroy();
}
