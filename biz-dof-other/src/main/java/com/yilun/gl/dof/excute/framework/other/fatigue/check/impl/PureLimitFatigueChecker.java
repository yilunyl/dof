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
 * 仅处理曝光限制疲劳度
 */
@Slf4j
@Component
public class PureLimitFatigueChecker extends FatigueCheck {
    @Override
    public Integer checkRuleType() {
        return FatigueRuleTypeEnum.PURE_LIMIT.getCode();
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
            Integer period = ruleTimeConfig.getStatisticalPeriod();
            Long frequency = ruleTimeConfig.getFrequencyThreshold();
            Long curIndex = accumTimeArray.getLeft();
            List<Long> timeArray = accumTimeArray.getRight();
            //获取总的数量
            Long totalCount = this.accumLatest(ruleAction, period, curIndex, timeArray, ruleDimensionConfig);

            if (frequency <= totalCount) {
                //命中疲劳度
                return FuncRetWrapper.success(Boolean.TRUE);
            } else {
                return FuncRetWrapper.success(Boolean.FALSE);
            }
        } catch (Exception e) {
            log.error("PureLimitFatigueChecker_Exception ", e);
            return FuncRetWrapper.success(Boolean.FALSE);
        }
    }

}
