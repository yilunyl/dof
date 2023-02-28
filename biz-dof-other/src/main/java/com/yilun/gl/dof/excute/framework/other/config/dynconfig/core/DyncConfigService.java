package com.yilun.gl.dof.excute.framework.other.config.dynconfig.core;

import com.yilun.gl.dof.excute.framework.other.config.dynconfig.core.entity.DyncConfigTypeEnum;
import com.yilun.gl.dof.excute.framework.other.config.dynconfig.core.entity.RefreshConfgiEntity;

/**
 * @Auther: gl
 * @Date: 2020/10/22 17:53
 * @Description:
 */
public interface DyncConfigService {

    /**
     * 根据客户端和path创建地址
     *
     * @param path 配置地址
     */
    byte[] createNodeCache(String path);

    /**
     * 刷新值
     */
    void refreshProperties(RefreshConfgiEntity refresh);

    /**
     * 销毁
     */
    void destroy();

    /**
     * 获取配置类型
     *
     * @return DyncConfigTypeEnum
     */
    DyncConfigTypeEnum getConfigType();
}
