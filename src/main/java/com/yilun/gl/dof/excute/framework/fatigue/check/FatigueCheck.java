package com.yilun.gl.dof.excute.framework.fatigue.check;


import com.alibaba.fastjson.JSON;;
import com.yilun.gl.dof.excute.framework.fatigue.FatigueSummaryDomainEntity;
import com.yilun.gl.dof.excute.framework.fatigue.RuleDimensionConfig;
import com.yilun.gl.dof.excute.framework.fatigue.RuleTimeConfig;
import com.yilun.gl.dof.excute.framework.fatigue.summary.AccumSummaryEntity;
import com.yilun.gl.dof.excute.framework.fatigue.summary.AccumWithTime;
import com.yilun.gl.dof.excute.framework.util.FuncRetWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.yilun.gl.dof.excute.framework.fatigue.FatiguePeriodTypeEnum.*;


@Slf4j
public abstract class FatigueCheck {


    /**
     * @return FatigueActionTypeEnum
     * @see
     */
    public Integer checkRuleType() {
        return -1;
    }

    /**
     * 处理实际的校验逻辑
     *
     * @param data                数据库中读取的数据记录
     * @param ruleDimensionConfig 配置的规则
     * @param fatigueCheckRequest 行为类型
     * @return 是否命中疲劳度 ture命中疲劳度
     */
    public FuncRetWrapper<Boolean> doCheck(FatigueSummaryDomainEntity data, RuleDimensionConfig ruleDimensionConfig, FatigueCheckRequest fatigueCheckRequest) {
        return FuncRetWrapper.success(Boolean.FALSE);
    }


    /**
     * 根据规则获取对应的索引和对应的数据
     *
     * @param accumSummary
     * @param accumWithTime
     * @param ruleTimeConfig
     * @param currentTime
     * @return
     */
    protected Pair<Long, List<Long>> getAccumTimeArray(AccumSummaryEntity accumSummary, AccumWithTime accumWithTime, RuleTimeConfig ruleTimeConfig, Long currentTime) {

        Integer fatiguePeriodType = ruleTimeConfig.getPeriodUnit();
        if (Objects.isNull(fatiguePeriodType)) {
            return null;
        }
        if (Objects.isNull(ruleTimeConfig.getProcessingPeriod())) {
            ruleTimeConfig.setProcessingPeriod(0);
        }
        if (Objects.isNull(ruleTimeConfig.getStatisticalPeriod())) {
            ruleTimeConfig.setStatisticalPeriod(0);
        }
        if (Objects.isNull(ruleTimeConfig.getFrequencyThreshold())) {
            ruleTimeConfig.setFrequencyThreshold(10L);
        }
        if (Objects.equals(fatiguePeriodType, MINUTE.getCode())) {
            Integer harfMin = accumSummary.getAccumDef().getMinuteWindowSize() / 2;
            if (ruleTimeConfig.getProcessingPeriod() > harfMin) {
                log.error("FatigueCheck getAccumTimeArray_errorMINUTE reason=超过数组范围-取数组设定值={}|疲劳度周期={}|疲劳度规则={}", harfMin, fatiguePeriodType, JSON.toJSONString(ruleTimeConfig));
                ruleTimeConfig.setProcessingPeriod(harfMin);
            }
            if (ruleTimeConfig.getStatisticalPeriod() > harfMin) {
                log.error("FatigueCheck getAccumTimeArray_errorMINUTE reason=超过数组范围-取数组设定值={}|疲劳度周期={}|疲劳度规则={}", harfMin, fatiguePeriodType, JSON.toJSONString(ruleTimeConfig));
                ruleTimeConfig.setStatisticalPeriod(harfMin);
            }
            return Pair.of(accumSummary.getMinuteIndex(currentTime, accumSummary.getAccumDef()), accumWithTime.getMinuteAccum());
        } else if (Objects.equals(fatiguePeriodType, HOUR.getCode())) {
            Integer harfHour = accumSummary.getAccumDef().getHourWindowSize() / 2;
            if (ruleTimeConfig.getProcessingPeriod() > harfHour) {
                log.error("FatigueCheck getAccumTimeArray_errorHOUR reason=超过数组范围-取数组设定值={}|疲劳度周期={}|疲劳度规则={}", harfHour, fatiguePeriodType, JSON.toJSONString(ruleTimeConfig));
                ruleTimeConfig.setProcessingPeriod(harfHour);
            }
            if (ruleTimeConfig.getStatisticalPeriod() > harfHour) {
                log.error("FatigueCheck getAccumTimeArray_errorHOUR reason=超过数组范围-取数组设定值={}|疲劳度周期={}|疲劳度规则={}", harfHour, fatiguePeriodType, JSON.toJSONString(ruleTimeConfig));
                ruleTimeConfig.setStatisticalPeriod(harfHour);
            }
            return Pair.of(accumSummary.getHourIndex(currentTime, accumSummary.getAccumDef()), accumWithTime.getHourAccum());
        } else if (Objects.equals(fatiguePeriodType, DAY.getCode())) {
            Integer harfDay = accumSummary.getAccumDef().getDayWindowSize() / 2;
            if (ruleTimeConfig.getProcessingPeriod() > harfDay) {
                log.error("FatigueCheck getAccumTimeArray_errorDAY reason=超过数组范围-取数组设定值={}|疲劳度周期={}|疲劳度规则={}", harfDay, fatiguePeriodType, JSON.toJSONString(ruleTimeConfig));
                ruleTimeConfig.setProcessingPeriod(harfDay);
            }
            if (ruleTimeConfig.getStatisticalPeriod() > harfDay) {
                log.error("FatigueCheck getAccumTimeArray_errorDAY reason=超过数组范围-取数组设定值={}|疲劳度周期={}|疲劳度规则={}", harfDay, fatiguePeriodType, JSON.toJSONString(ruleTimeConfig));
                ruleTimeConfig.setStatisticalPeriod(harfDay);
            }
            return Pair.of(accumSummary.getDayIndex(currentTime, accumSummary.getAccumDef()), accumWithTime.getDayAccum());
        } else if (Objects.equals(fatiguePeriodType, WEEK.getCode())) {
            Integer harfWeek = accumSummary.getAccumDef().getWeekWindowSize() / 2;

            if (ruleTimeConfig.getProcessingPeriod() > harfWeek) {
                log.error("FatigueCheck getAccumTimeArray_errorWEEK reason=超过数组范围-取数组设定值={}|疲劳度周期={}|疲劳度规则={}", harfWeek, fatiguePeriodType, JSON.toJSONString(ruleTimeConfig));
                ruleTimeConfig.setProcessingPeriod(harfWeek);
            }
            if (ruleTimeConfig.getStatisticalPeriod() > harfWeek) {
                log.error("FatigueCheck getAccumTimeArray_errorWEEK reason=超过数组范围-取数组设定值={}|疲劳度周期={}|疲劳度规则={}", harfWeek, fatiguePeriodType, JSON.toJSONString(ruleTimeConfig));
                ruleTimeConfig.setStatisticalPeriod(harfWeek);
            }
            return Pair.of(accumSummary.getWeekIndex(currentTime, accumSummary.getAccumDef()), accumWithTime.getWeekAccum());
        } else if (Objects.equals(fatiguePeriodType, MONTH.getCode())) {
            Integer harfMonth = accumSummary.getAccumDef().getMonthWindowSize() / 2;

            if (ruleTimeConfig.getProcessingPeriod() > harfMonth) {
                log.error("FatigueCheck getAccumTimeArray_errorMONTH reason=超过数组范围-取数组设定值={}|疲劳度周期={}|疲劳度规则={}", harfMonth, fatiguePeriodType, JSON.toJSONString(ruleTimeConfig));
                ruleTimeConfig.setProcessingPeriod(harfMonth);
            }
            if (ruleTimeConfig.getStatisticalPeriod() > harfMonth) {
                log.error("FatigueCheck getAccumTimeArray_errorMONTH reason=超过数组范围-取数组设定值={}|疲劳度周期={}|疲劳度规则={}", harfMonth, fatiguePeriodType, JSON.toJSONString(ruleTimeConfig));
                ruleTimeConfig.setStatisticalPeriod(harfMonth);
            }
            return Pair.of(accumSummary.getMonthIndex(currentTime, accumSummary.getAccumDef()), accumWithTime.getMonthAccum());
        } else if (Objects.equals(fatiguePeriodType, YEAR.getCode())) {
            Integer harfYear = accumSummary.getAccumDef().getYearWindowSize() / 2;

            if (ruleTimeConfig.getProcessingPeriod() > harfYear) {
                log.error("FatigueCheck getAccumTimeArray_errorYEAR reason=超过数组范围-取数组设定值={}|疲劳度周期={}|疲劳度规则={}", harfYear, fatiguePeriodType, JSON.toJSONString(ruleTimeConfig));
                ruleTimeConfig.setProcessingPeriod(harfYear);
            }
            if (ruleTimeConfig.getStatisticalPeriod() > harfYear) {
                log.error("FatigueCheck getAccumTimeArray_errorYEAR reason=超过数组范围-取数组设定值={}|疲劳度周期={}|疲劳度规则={}", harfYear, fatiguePeriodType, JSON.toJSONString(ruleTimeConfig));
                ruleTimeConfig.setStatisticalPeriod(harfYear);
            }
            return Pair.of(accumSummary.getYearIndex(currentTime, accumSummary.getAccumDef()), accumWithTime.getMonthAccum());
        } else if (Objects.equals(fatiguePeriodType, QUARTER.getCode())) {
            return null;
        }
        return null;
    }


    /**
     * eg.根据当前索引获取往前递推几天的数据总和
     *
     * @param statisticalPeriod 统计周期
     * @param curIndex
     * @param timeArray
     * @return
     */
    protected Long accumLatest(String action, Integer statisticalPeriod, Long curIndex, List<Long> timeArray, RuleDimensionConfig rule) {

        if (statisticalPeriod > timeArray.size()) {
            statisticalPeriod = timeArray.size();
        }

        Long totalCount = 0L;
        List<Long> tmp = new ArrayList<>();
        if (curIndex >= statisticalPeriod) {
            //累加求和
            for (int i = curIndex.intValue() - statisticalPeriod + 1; i <= curIndex; i++) {
                totalCount = totalCount + timeArray.get(i);
                tmp.add(timeArray.get(i));
            }
        } else {
            //求前半段
            for (int i = 0; i < curIndex; i++) {
                totalCount = totalCount + timeArray.get(i);
                tmp.add(timeArray.get(i));
            }
            //求后半段
            int startIndex = timeArray.size() - (statisticalPeriod - curIndex.intValue());
            for (int i = startIndex; i < timeArray.size(); i++) {
                totalCount = totalCount + timeArray.get(i);
                tmp.add(timeArray.get(i));
            }
        }
        log.info("accumLatest event={}|position={}|ruleId={}|ruleName={}|当前统计的类型是={}|周期长度是={}|是当前的索引={}|统计的总次数是={}|统计周期内明细次数是={}",
                rule.getEvent(),rule.getPosition(),rule.getRuleId(), rule.getRuleName(), action, statisticalPeriod, curIndex, totalCount, JSON.toJSONString(tmp));
        return totalCount;
    }

    protected static void doCheckLog(RuleDimensionConfig rule, String checkResult, String reason){
        log.info("FatigueCheck_do_check event={}|position={}|ruleId={}|ruleName={}|checkResult={}|reason={}|",
                rule.getEvent(),rule.getPosition(),rule.getRuleId(), rule.getRuleName(), checkResult, reason);
    }
}
