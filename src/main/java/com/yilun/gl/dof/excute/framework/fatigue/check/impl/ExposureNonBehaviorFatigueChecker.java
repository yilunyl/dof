package com.yilun.gl.dof.excute.framework.fatigue.check.impl;

import com.yilun.gl.dof.excute.framework.fatigue.FatigueRuleTypeEnum;
import com.yilun.gl.dof.excute.framework.fatigue.FatigueSummaryDomainEntity;
import com.yilun.gl.dof.excute.framework.fatigue.RuleDimensionConfig;
import com.yilun.gl.dof.excute.framework.fatigue.RuleTimeConfig;
import com.yilun.gl.dof.excute.framework.fatigue.check.FatigueCheck;
import com.yilun.gl.dof.excute.framework.fatigue.check.FatigueCheckRequest;
import com.yilun.gl.dof.excute.framework.fatigue.summary.AccumSummaryEntity;
import com.yilun.gl.dof.excute.framework.fatigue.summary.AccumWithTime;
import com.yilun.gl.dof.excute.framework.util.FuncRetWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 连续曝光后不点击-一段时间不展示
 */
@Slf4j
@Component
public class ExposureNonBehaviorFatigueChecker extends FatigueCheck {
    @Override
    public Integer checkRuleType() {
        return FatigueRuleTypeEnum.SPECIFY_BEHAVIOR_SILENT.getCode();
    }

    @Override
    public FuncRetWrapper<Boolean> doCheck(FatigueSummaryDomainEntity data, RuleDimensionConfig ruleDimensionConfig, FatigueCheckRequest fatigueCheckRequest) {
        try {
            String currentAction = fatigueCheckRequest.getAction();
            Long currentTime = Objects.nonNull(fatigueCheckRequest.getFatigueCheckTime()) ? fatigueCheckRequest.getFatigueCheckTime() : System.currentTimeMillis() / 1000;
            String statisticalRuleAction = ruleDimensionConfig.getStatisticalAction();
            List<String> unExpectActions = ruleDimensionConfig.getExcludeActions();
            AccumSummaryEntity accumSummary = data.getAccumSummary();
            Map<String, AccumWithTime> actionAccumMap = accumSummary.getActionAccumMap();
            if (!actionAccumMap.containsKey(statisticalRuleAction)) {
                doCheckLog(ruleDimensionConfig, "默认通过", "暂无统计数据");
                return FuncRetWrapper.success(Boolean.FALSE);
            }

            AccumWithTime actionAccumWithTime = actionAccumMap.get(statisticalRuleAction);

            RuleTimeConfig ruleTimeConfig = ruleDimensionConfig.getRuleTimeConfig();
            if (Objects.isNull(ruleTimeConfig)) {
                doCheckLog(ruleDimensionConfig, "默认通过", "无具体配置规则");
                return FuncRetWrapper.success(Boolean.FALSE);
            }

            //统计配置的反向记录
            Long unEspectActionsStatisticalCount = statisticalUnEspectActionsLatest(actionAccumMap, unExpectActions, accumSummary, ruleDimensionConfig, currentTime);
            if (unEspectActionsStatisticalCount > 0L) {
                //说明存在点击行为，则该疲劳度校验成功
                return FuncRetWrapper.success(Boolean.FALSE);
            }

            //获取当前对应的索引和对应的数组
            Pair<Long, List<Long>> accumTimeArray = this.getAccumTimeArray(accumSummary, actionAccumWithTime, ruleTimeConfig, currentTime);
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
            Long statisticalTotalCount = this.accumLatest(statisticalRuleAction, statisticalPeriod, curIndex, timeArray, ruleDimensionConfig);

            if (frequency <= statisticalTotalCount) {
                //命中疲劳度
                return FuncRetWrapper.success(Boolean.TRUE);
            } else {
                //在用统计周期+处理周期在查找一次
                Long aLong = totalPeriodUnEspectActionsLatest(actionAccumMap, unExpectActions, accumSummary, ruleDimensionConfig, currentTime);
                Long totalPeriodCount = this.accumLatest(statisticalRuleAction, statisticalPeriod + processingPeriod, curIndex, timeArray, ruleDimensionConfig);
                if (frequency <= totalPeriodCount && aLong <= 0) {
                    //命中疲劳度
                    return FuncRetWrapper.success(Boolean.TRUE);
                }
            }
            return FuncRetWrapper.success(Boolean.FALSE);
        } catch (Exception e) {
            log.error("ExposureNonBehaviorFatigueChecker Exception ", e);
            return FuncRetWrapper.success(Boolean.FALSE);
        }
    }

    private Long statisticalUnEspectActionsLatest(Map<String, AccumWithTime> actionAccumMap, List<String> unEspectActions, AccumSummaryEntity accumSummary, RuleDimensionConfig ruleDimensionConfig, Long currentTime) {

        RuleTimeConfig ruleTimeConfig = ruleDimensionConfig.getRuleTimeConfig();

        for (String unEspectAction : unEspectActions) {
            if (!actionAccumMap.containsKey(unEspectAction)) {
                doCheckLog(ruleDimensionConfig, "默认通过", "暂无统计数据");
                continue;
            }
            AccumWithTime actionAccumWithTime = actionAccumMap.get(unEspectAction);
            Pair<Long, List<Long>> accumTimeArray = this.getAccumTimeArray(accumSummary, actionAccumWithTime, ruleTimeConfig, currentTime);
            Integer statisticalPeriod = ruleTimeConfig.getStatisticalPeriod();
            Integer processingPeriod = ruleTimeConfig.getProcessingPeriod();
            Long curIndex = accumTimeArray.getLeft();
            List<Long> timeArray = accumTimeArray.getRight();
            //使用统计周期进行查找
            Long statisticalTotalCount = this.accumLatest(unEspectAction, statisticalPeriod, curIndex, timeArray, ruleDimensionConfig);
            if (statisticalTotalCount > 0L) {
                //说明用户发生了点击行为
                return statisticalTotalCount;
            }
        }
        return 0L;
    }

    private Long totalPeriodUnEspectActionsLatest(Map<String, AccumWithTime> actionAccumMap, List<String> unEspectActions, AccumSummaryEntity accumSummary, RuleDimensionConfig ruleDimensionConfig, Long currentTime) {

        RuleTimeConfig ruleTimeConfig = ruleDimensionConfig.getRuleTimeConfig();

        for (String unEspectAction : unEspectActions) {
            if (!actionAccumMap.containsKey(unEspectAction)) {
                doCheckLog(ruleDimensionConfig, "默认通过", "暂无统计数据");
                continue;
            }
            AccumWithTime actionAccumWithTime = actionAccumMap.get(unEspectAction);
            Pair<Long, List<Long>> accumTimeArray = this.getAccumTimeArray(accumSummary, actionAccumWithTime, ruleTimeConfig, currentTime);
            Integer statisticalPeriod = ruleTimeConfig.getStatisticalPeriod();
            Integer processingPeriod = ruleTimeConfig.getProcessingPeriod();
            Long curIndex = accumTimeArray.getLeft();
            List<Long> timeArray = accumTimeArray.getRight();
            //使用统计周期进行查找
            Long statisticalTotalCount = this.accumLatest(unEspectAction, statisticalPeriod + processingPeriod, curIndex, timeArray, ruleDimensionConfig);
            if (statisticalTotalCount > 0L) {
                //说明用户发生了点击行为
                return statisticalTotalCount;
            }
        }
        return 0L;
    }
}
