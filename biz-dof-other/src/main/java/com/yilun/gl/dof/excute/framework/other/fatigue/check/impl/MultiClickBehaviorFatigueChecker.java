package com.yilun.gl.dof.excute.framework.other.fatigue.check.impl;

import com.gl.dof.excute.framework.base.util.FuncRetWrapper;
import com.yilun.gl.dof.excute.framework.other.fatigue.FatigueRuleTypeEnum;
import com.yilun.gl.dof.excute.framework.other.fatigue.FatigueSummaryDomainEntity;
import com.yilun.gl.dof.excute.framework.other.fatigue.RuleDimensionConfig;
import com.yilun.gl.dof.excute.framework.other.fatigue.RuleTimeConfig;
import com.yilun.gl.dof.excute.framework.other.fatigue.check.FatigueCheck;
import com.yilun.gl.dof.excute.framework.other.fatigue.check.FatigueCheckRequest;
import com.yilun.gl.dof.excute.framework.other.fatigue.summary.AccumSummaryEntity;
import com.yilun.gl.dof.excute.framework.other.fatigue.summary.AccumWithTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 多次点击行为-一段时间不展示
 */
@Slf4j
@Component
public class MultiClickBehaviorFatigueChecker extends FatigueCheck {
    @Override
    public Integer checkRuleType() {
        return FatigueRuleTypeEnum.CLICK_BEHAVIOR_SILENT.getCode();
    }

    @Override
    public FuncRetWrapper<Boolean> doCheck(FatigueSummaryDomainEntity data, RuleDimensionConfig ruleDimensionConfig, FatigueCheckRequest fatigueCheckRequest) {
        try {
            String currentAction = fatigueCheckRequest.getAction();
            String ruleAction = ruleDimensionConfig.getStatisticalAction();
            Long currentTime = Objects.nonNull(fatigueCheckRequest.getFatigueCheckTime()) ? fatigueCheckRequest.getFatigueCheckTime() : System.currentTimeMillis() / 1000;


            AccumSummaryEntity accumSummary = data.getAccumSummary();
            Map<String, AccumWithTime> actionAccumMap = accumSummary.getActionAccumMap();
            if (!actionAccumMap.containsKey(ruleAction)) {
                doCheckLog(ruleDimensionConfig, "默认通过", "暂无统计数据");
                return FuncRetWrapper.success(Boolean.FALSE);
            }
            AccumWithTime accumWithTime = actionAccumMap.get(ruleAction);

            RuleTimeConfig ruleTimeConfig = ruleDimensionConfig.getRuleTimeConfig();
            if (Objects.isNull(ruleTimeConfig)) {
                doCheckLog(ruleDimensionConfig, "默认通过", "无具体配置规则");
                return FuncRetWrapper.success(Boolean.FALSE);
            }
            //获取当前对应的索引和对应的数组
            Pair<Long, List<Long>> accumTimeArray = this.getAccumTimeArray(accumSummary, accumWithTime, ruleTimeConfig, currentTime);
            if (Objects.isNull(accumTimeArray)) {
                doCheckLog(ruleDimensionConfig, "默认通过", "解析时间类型错误,无法匹配时间类型");
                return FuncRetWrapper.success(Boolean.FALSE);
            }
            Integer statisticalPeriod = ruleTimeConfig.getStatisticalPeriod();
            Integer processingPeriod = ruleTimeConfig.getProcessingPeriod();
            Long frequency = ruleTimeConfig.getFrequencyThreshold();
            Long curIndex = accumTimeArray.getLeft();
            List<Long> timeArray = accumTimeArray.getRight();
            //使用统计周期进行查找
            Long statisticalTotalCount = this.accumLatest(ruleAction, statisticalPeriod, curIndex, timeArray, ruleDimensionConfig);
            if (frequency <= statisticalTotalCount) {
                //命中疲劳度
                return FuncRetWrapper.success(Boolean.TRUE);
            } else {
                //在用统计周期+处理周期在查找一次
                Long totalPeriodCount = this.accumLatest(ruleAction, statisticalPeriod + processingPeriod, curIndex, timeArray, ruleDimensionConfig);
                if (frequency <= totalPeriodCount) {
                    //命中疲劳度
                    return FuncRetWrapper.success(Boolean.TRUE);
                }
            }
            return FuncRetWrapper.success(Boolean.FALSE);
        } catch (Exception e) {
            log.error("MultiClickBehaviorFatigueChecker_Exception ", e);
            return FuncRetWrapper.success(Boolean.FALSE);
        }
    }
}
