package com.yilun.gl.dof.excute.framework.other.config.dynconfig.core;

import com.gl.dof.core.excute.framework.exception.DofServiceException;
import com.yilun.gl.dof.excute.framework.other.constants.ApiConstant;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: gl
 * @Date: 2020/10/22 19:22
 * @Description:
 */
@Component
public class DyncConfigContext {
    //默认值
    private static Integer DEFAULT_SIZE = 2;

    private final Map<Integer, DyncConfigService> strategyMap = new ConcurrentHashMap<>(DEFAULT_SIZE);

    /**
     * 初始化map
     *
     * @param strategyList springBoot启动时识别到的DyncConfigService的实现类
     */
    private DyncConfigContext(List<DyncConfigService> strategyList) {
        if (CollectionUtils.isEmpty(strategyList)) {
            throw DofServiceException.build(ApiConstant.CODE_FAIL, "fail init DyncConfigContext");
        }
        for (DyncConfigService strategy : strategyList) {
            strategyMap.put(strategy.getConfigType().value(), strategy);
        }

    }

    /**
     * 可能返回空
     *
     * @param id beanId
     * @return bean
     */
    public DyncConfigService chooseConfig(Integer id) {
        return strategyMap.getOrDefault(id, null);
    }

    public List<DyncConfigService> getAll() {
        List<DyncConfigService> resList = new ArrayList<>(2);
        resList.addAll(strategyMap.values());
        return resList;
    }
}
