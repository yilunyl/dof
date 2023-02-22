package com.yilun.gl.dof.excute.framework.fatigue.summary;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * 注意 该类中的所有字段 只能新增 不能删除 也不能使用@Deprecated注解，不能添加复杂的集合结构 ==> 会导致序列化失败
 */
@Data
public class AccumSummaryEntity implements Serializable {

    /**
     * key 是行为类型 val是对应的统计信息
     */
//    @Tag(1)
    private Map<String, AccumWithTime> actionAccumMap;

//    @Tag(2)
    private AccumDef accumDef;

//    @Tag(3)
    private AccumTs accumTs;


    public void doAccum(int count, Long updateTime, String actionId) {
        AccumWithTime accumWithTime;
        if (actionAccumMap.containsKey(actionId)) {
            accumWithTime = actionAccumMap.get(actionId);
        } else {
            accumWithTime = initAccumWithTime(this.accumDef);
            actionAccumMap.put(actionId, accumWithTime);
        }

        AccumTs accumTs1 = this.getAccumTs();
        AccumDef accumDef1 = this.getAccumDef();

        //处理时间
        accumTs1.setTime(updateTime);

        //处理天
        Long dayIndex = getDayIndex(updateTime, accumDef1);
        accumTs1.setDay(dayIndex);
        Long oldDayVal = accumWithTime.getDayAccum().get(dayIndex.intValue());
        accumWithTime.getDayAccum().set(dayIndex.intValue(), oldDayVal + count);

        //处理小时
        Long hourIndex = getHourIndex(updateTime, accumDef1);
        accumTs1.setHour(hourIndex);
        Long oldHourVal = accumWithTime.getHourAccum().get(hourIndex.intValue());
        accumWithTime.getHourAccum().set(hourIndex.intValue(), oldHourVal + count);

        //处理分钟 每个格子代表5分钟
        Long minuteIndex = getMinuteIndex(updateTime, accumDef1);
        accumTs1.setMinute(minuteIndex);
        Long oldminuteVal = accumWithTime.getMinuteAccum().get(minuteIndex.intValue());
        accumWithTime.getMinuteAccum().set(minuteIndex.intValue(), oldminuteVal + count);

        //处理月
        Long monthIndex = getMonthIndex(updateTime, accumDef1);
        accumTs1.setMonth(monthIndex);
        Long oldmonthVal = accumWithTime.getMonthAccum().get(monthIndex.intValue());
        accumWithTime.getMonthAccum().set(monthIndex.intValue(), oldmonthVal + count);

        //处理周
        Long weekIndex = getWeekIndex(updateTime, accumDef1);
        accumTs1.setWeek(weekIndex);
        Long oldweekVal = accumWithTime.getWeekAccum().get(weekIndex.intValue());
        accumWithTime.getWeekAccum().set(weekIndex.intValue(), oldweekVal + count);

        //处理年
        Long yearIndex = getYearIndex(updateTime, accumDef1);
        accumTs1.setYear(yearIndex);
        Long oldYearVal = accumWithTime.getYearAccum().get(yearIndex.intValue());
        accumWithTime.getYearAccum().set(yearIndex.intValue(), oldYearVal + count);

        //处理总数量
        if(accumDef1.getKeepTotalNum()) {
            Long newCount = accumWithTime.getTotalNum() + count;
            accumWithTime.setTotalNum(newCount);
        }
    }

    private AccumWithTime initAccumWithTime(AccumDef accumDef) {
        AccumWithTime accumWithTime = new AccumWithTime();

        accumWithTime.setDayAccum(initZero(accumDef.getDayWindowSize()));
        accumWithTime.setHourAccum(initZero(accumDef.getHourWindowSize()));
        accumWithTime.setMinuteAccum(initZero(accumDef.getMinuteWindowSize()));
        accumWithTime.setMonthAccum(initZero(accumDef.getMonthWindowSize()));
        accumWithTime.setWeekAccum(initZero(accumDef.getWeekWindowSize()));
        accumWithTime.setYearAccum(initZero(accumDef.getYearWindowSize()));
        accumWithTime.setTotalNum(0L);
        return accumWithTime;
    }


    public Long getDayIndex(Long updateTime, AccumDef accumDef) {
        long days = updateTime / (24 * 3600);
        Long dayIndex = days % accumDef.getDayWindowSize();
        return dayIndex;
    }

    public Long getHourIndex(Long updateTime, AccumDef accumDef) {
        long hours = updateTime / (3600);
        Long hourIndex = hours % accumDef.getHourWindowSize();
        return hourIndex;
    }

    public Long getMinuteIndex(Long updateTime, AccumDef accumDef) {
        //最小单位是5分钟
        long perSzie = 5;
        long minutes = updateTime / (60 * perSzie);
        Long minuteIndex = minutes % accumDef.getMinuteWindowSize();
        return minuteIndex;
    }

    public Long getMonthIndex(Long updateTime, AccumDef accumDef) {
        long months = updateTime / (24 * 3600 * 30);
        Long monthIndex = months % accumDef.getMonthWindowSize();
        return monthIndex;
    }

    public Long getWeekIndex(Long updateTime, AccumDef accumDef) {
        long weeks = updateTime / (24 * 3600 * 7);
        Long weekIndex = weeks % accumDef.getWeekWindowSize();
        return weekIndex;
    }

    public Long getYearIndex(Long updateTime, AccumDef accumDef) {
        long years = updateTime / (24 * 3600 * 30 * 12);
        Long yearIndex = years % accumDef.getYearWindowSize();
        return yearIndex;
    }

    private List<Long> initZero(Integer arraySize) {
        List<Long> array = new ArrayList<>(arraySize);
        for (int i = 0; i < arraySize; i++) {
            array.add(i, 0L);
        }
        return array;
    }

}
