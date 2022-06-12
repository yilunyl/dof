package com.yilun.gl.dof.excute.framework.config.dynconfig.impl.etcd;

import com.yilun.gl.dof.excute.framework.config.dynconfig.core.BasicDyncConfigService;
import com.yilun.gl.dof.excute.framework.config.dynconfig.core.entity.DyncConfigTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Auther: gl
 * @Date: 2020/10/22 15:45
 * @Description:
 */
@Component
@Slf4j
public class EtcdDyncConfigServiceImpl extends BasicDyncConfigService {

    @Override
    protected void createConfigClient() {

    }

    @Override
    protected byte[] initFirstData(String path) {
        return new byte[0];
    }

    @Override
    protected void registerListener(String path) {

    }

    @Override
    protected DyncConfigTypeEnum getDyncConfigType() {
        return DyncConfigTypeEnum.ETCD_00;
    }

    @Override
    protected void doDestroy() {

    }
}
