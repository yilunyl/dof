package com.yilun.gl.dof.excute.framework.core.rule;

import com.yilun.gl.dof.excute.framework.core.logic.ParrentLogicUnit;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @ClassName BasicLogicRule
 * @Author gule.gl
 * @Date 2021/6/19 2:26 下午
 * @Description todo 该方法需要从配置中心读取 所以此处需要重构一下
 * @Version 1.0
 */
public abstract class BasicLogicRule{


    /**
     * 缓存执行单元->执行规则
     */
    private LogicRuleContainer logicRuleContainer = LogicRuleContainer.getInstance();


    @PostConstruct
    void initLogicRule(){
        convertAndRegister(addDataPrepareRule());
        convertAndRegister(addDataCheckRule());
        convertAndRegister(addDataProcessorRule());
        convertAndRegister(addDataAfterProcessorRule());
        convertAndRegister(addDataEventRule());
    }

    private void convertAndRegister(Map<LogicRule, ParrentLogicUnit> addDataEventRule){
        for(Map.Entry<LogicRule, ParrentLogicUnit> entry:addDataEventRule.entrySet()) {
            logicRuleContainer.registerRule(entry.getValue(), entry.getKey());
        }

    }


    protected abstract Map<LogicRule, ParrentLogicUnit> addDataPrepareRule();

    protected abstract Map<LogicRule, ParrentLogicUnit> addDataCheckRule();

    protected abstract Map<LogicRule, ParrentLogicUnit> addDataProcessorRule();

    protected abstract Map<LogicRule, ParrentLogicUnit>  addDataAfterProcessorRule();

    protected abstract Map<LogicRule, ParrentLogicUnit> addDataEventRule();

}
