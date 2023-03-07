package com.gl.dof.core.excute.framework.rule;

import com.gl.dof.core.excute.framework.logic.LogicUnit;

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

    private void convertAndRegister(Map<LogicRule, LogicUnit> addDataEventRule){
        for(Map.Entry<LogicRule, LogicUnit> entry:addDataEventRule.entrySet()) {
            logicRuleContainer.registerRule(entry.getValue(), entry.getKey());
        }

    }


    protected abstract Map<LogicRule, LogicUnit> addDataPrepareRule();

    protected abstract Map<LogicRule, LogicUnit> addDataCheckRule();

    protected abstract Map<LogicRule, LogicUnit> addDataProcessorRule();

    protected abstract Map<LogicRule, LogicUnit>  addDataAfterProcessorRule();

    protected abstract Map<LogicRule, LogicUnit> addDataEventRule();

}
