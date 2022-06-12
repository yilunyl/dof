package com.yilun.gl.dof.excute.framework.config.dynconfig.core.entity;

import org.springframework.context.ApplicationEvent;

/**
 * @Auther: gl
 * @Date: 2020/10/22 17:48
 * @Description:
 */
public class ConfigChangedEvent extends ApplicationEvent {

    public ConfigChangedEvent(RefreshConfgiEntity source) {
        super(source);
    }


    public RefreshConfgiEntity getServiceBean() {
        return (RefreshConfgiEntity) super.getSource();
    }
}
