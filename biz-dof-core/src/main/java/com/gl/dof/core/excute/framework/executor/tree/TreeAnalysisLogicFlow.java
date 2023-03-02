package com.gl.dof.core.excute.framework.executor.tree;

import com.alibaba.fastjson.JSON;
import com.gl.dof.core.excute.framework.content.TreeWrapper;
import com.gl.dof.core.excute.framework.logic.DomainServiceUnit;
import com.google.common.collect.Lists;
import com.yilun.gl.dof.excute.framework.base.util.FuncRetWrapper;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.*;
import java.util.concurrent.atomic.LongAdder;

public class TreeAnalysisLogicFlow {

    private TreeAnalysisLogicFlow(){
    }

    public static TreeAnalysisLogicFlow single = null;

    public static TreeAnalysisLogicFlow getInstance(){
        if(Objects.isNull(single)){
            single = new TreeAnalysisLogicFlow();
        }
        return single;
    }

    private final static Logger logger = LoggerFactory.getLogger(TreeAnalysisLogicFlow.class);

    public static TreeWrapper analysisLogicFlow(String logicFlow, ApplicationContext applicationContext) {
        return TreeAnalysisLogicFlow.getInstance().doAnalysisLogicFlow(logicFlow, applicationContext);
    }



    /**
     *  [a,d,b],ad,
     * @param logicFlow
     * @param applicationContext
     * @return
     */
    private TreeWrapper doAnalysisLogicFlow(String logicFlow, ApplicationContext applicationContext) {
        if(StringUtils.isBlank(logicFlow) || Objects.isNull(applicationContext)){
            return null;
        }
        FuncRetWrapper<LinkedHashMap<String, List<String>>> serialBeanNameMap = getSerialBeanNameMap(logicFlow);
        if(!serialBeanNameMap.isSuccess()){
            logger.error("analysisLogicFlow_error_msg {}", serialBeanNameMap.getMessage());
            return null;
        }
        logger.info("ExecutorFactory logicFlow={}, analysisLogicFlow={}", logicFlow, JSON.toJSONString(serialBeanNameMap));
        TreeWrapper treeWrapper = new TreeWrapper();
        for(Map.Entry<String, List<String>> entry : serialBeanNameMap.getData().entrySet()){
            List<String> value = entry.getValue();
            List<DomainServiceUnit> units = new ArrayList<>();
            for(String name : value){
                DomainServiceUnit bean = getBean(name, applicationContext);
                if(Objects.isNull(bean)){
                    return null;
                }
                units.add(bean);
            }
            treeWrapper.parallelAdd(units);
        }
        return treeWrapper;
    }

    /**
     * 获取并行里面的bean名字
     *
     * @param substring
     * @return
     */
    private List<String> getParallelBeanName(String substring){
        List<String> units = new ArrayList<>();
        String[] split = substring.trim().split(",");

        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            units.add(s.trim());
        }
        return units;
    }

    private FuncRetWrapper<LinkedHashMap<String, List<String>>> getSerialBeanNameMap(String logicFlow){
        Counter counter = new Counter();
        LinkedHashMap<String, List<String>> logicNameMap = new LinkedHashMap<>();

        int currentPoint = 0;
        int recordPoint = 0;
        boolean handleParalle = false;
        while(currentPoint < logicFlow.length()){
            //开始 [
            if(Objects.equals(logicFlow.charAt(currentPoint), CharUtils.toChar("["))){
                if(recordPoint < currentPoint){
                    //截取 ,直接到[的情况
                    String beanName = getNameFromStartToEnd(logicFlow, recordPoint, currentPoint);
                    if(StringUtils.isNotBlank(beanName)){
                        logicNameMap.put(counter.getAndIncrement(), Lists.newArrayList(beanName));
                    }
                }
                recordPoint = currentPoint;
                handleParalle = true;
            }
            //停止 ]
            if(Objects.equals(logicFlow.charAt(currentPoint), CharUtils.toChar("]"))){
                List<String> parallelBean = getParallelBeanName(logicFlow.substring(recordPoint + 1, currentPoint));
                logicNameMap.put(counter.getAndIncrement(), parallelBean);
                handleParalle = false;
                recordPoint = currentPoint;
            }
            //常规 找逗号  abd,df,aer,
            if(!handleParalle && Objects.equals(logicFlow.charAt(currentPoint), CharUtils.toChar(","))){
                if(Objects.equals(logicFlow.charAt(recordPoint), CharUtils.toChar("]"))){
                    //说明中间的是一个bean名字
                    String beanName = getNameFromStartToEnd(logicFlow, recordPoint, currentPoint);
                    if(StringUtils.isNotBlank(beanName)){
                        logicNameMap.put(counter.getAndIncrement(), Lists.newArrayList(beanName));
                    }
                }
                if(Objects.equals(logicFlow.charAt(recordPoint), CharUtils.toChar(",")) || recordPoint == 0 ){
                    //说明中间的是一个bean名字
                    String beanName = getNameFromStartToEnd(logicFlow, recordPoint, currentPoint);
                    if(StringUtils.isNotBlank(beanName)){
                        logicNameMap.put(counter.getAndIncrement(), Lists.newArrayList(beanName));
                    }
                }
                recordPoint = currentPoint;
            }
            //结尾的处理
            if((currentPoint == logicFlow.length() -1) && recordPoint < currentPoint ){
                String beanName = getNameFromStartToEnd(logicFlow, recordPoint, currentPoint + 1);
                if(StringUtils.isNotBlank(beanName)){
                    logicNameMap.put(counter.getAndIncrement(), Lists.newArrayList(beanName));
                }
            }
            currentPoint++;
        }

        return FuncRetWrapper.success(logicNameMap);
    }

    private String getNameFromStartToEnd(String logicFlow, int recordPoint, int currentPoint){
        return logicFlow.substring(recordPoint == 0 ? recordPoint : recordPoint + 1, currentPoint );
    }

    private DomainServiceUnit getBean(String name, ApplicationContext applicationContext){
        if(StringUtils.isBlank(name)){
            return null;
        }
        boolean match = applicationContext.isTypeMatch(name.trim(), DomainServiceUnit.class);
        if(!match){
            return null;
        }
        return applicationContext.getBean(name.trim(), DomainServiceUnit.class);
    }

    private static class Counter extends LongAdder {
        Counter(){
            super();
            this.reset();
        }

        public String getAndIncrement(){
            long l = this.longValue();
            this.increment();;
            return String.valueOf(l);
        }
    }

    public static void main(String[] args){
        FuncRetWrapper serialBeanNameMap = TreeAnalysisLogicFlow.getInstance().getSerialBeanNameMap("ad,d,[123,546,34],234,5345,5");
        System.out.println(serialBeanNameMap.toString());
        serialBeanNameMap = TreeAnalysisLogicFlow.getInstance().getSerialBeanNameMap("ad,d[123,546,34]234,5345,5");
        System.out.println(serialBeanNameMap.toString());
    }
}
