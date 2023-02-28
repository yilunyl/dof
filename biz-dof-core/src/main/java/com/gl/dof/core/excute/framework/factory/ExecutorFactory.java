package com.gl.dof.core.excute.framework.factory;

import com.gl.dof.core.excute.framework.annotation.DofReference;
import com.gl.dof.core.excute.framework.content.TreeWrapper;
import com.gl.dof.core.excute.framework.entry.AbstractApplication;
import com.gl.dof.core.excute.framework.executor.ExecutorType;
import com.gl.dof.core.excute.framework.executor.tree.TreeApplicationExecutor;
import com.gl.dof.core.excute.framework.logic.DomainServiceUnit;
import com.google.common.collect.Lists;
import com.yilun.gl.dof.excute.framework.base.util.FuncRetWrapper;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class ExecutorFactory<REQ, RES> {

    private final static Logger logger = LoggerFactory.getLogger(ExecutorFactory.class);

    private ApplicationContext applicationContext;
    private ExecutorFactory(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }

    public static ExecutorFactory single = null;

    public static ExecutorFactory getInstance(ApplicationContext applicationContext){
        if(Objects.isNull(single)){
            single = new ExecutorFactory(applicationContext);
        }
        return single;
    }

    public AbstractApplication getExecutor(DofReference annotation) {
        if(Objects.isNull(applicationContext)){
            return null;
        }
        AbstractApplication abstractApplication;
        ExecutorType executorType = annotation.execTypeSel();
        if(Objects.equals(executorType, ExecutorType.TREE_LIKE)){
            abstractApplication = new TreeDefaultExecutor();
            //解析执行链
            TreeWrapper treeWrapper1 = analysisLogicFlow(annotation.logicFlow(), applicationContext);
            abstractApplication.setTreeWrapperI(treeWrapper1);
        }else{
            abstractApplication = new DagDefaultExecutor();
            //ToDo 图的链需要单独解析
        }
        if(!abstractApplication.initSuccess()){
            return null;
        }
        if(annotation.corePoolSize() > 1){
            abstractApplication.setCorePoolSize(annotation.corePoolSize());
        }
        //设置线程池
        if(!annotation.useCommonPool()){
            abstractApplication.setExecutorName(annotation.funcKey());
        }
        return abstractApplication;
    }

    /**
     *  [a,d,b],ad,
     * @param logicFlow
     * @param applicationContext
     * @return
     */
    private TreeWrapper analysisLogicFlow(String logicFlow, ApplicationContext applicationContext) {
        if(StringUtils.isBlank(logicFlow) || Objects.isNull(applicationContext)){
            return null;
        }
        FuncRetWrapper<LinkedHashMap<String, List<String>>> serialBeanNameMap = getSerialBeanNameMap(logicFlow);
        if(!serialBeanNameMap.isSuccess()){
            logger.error("analysisLogicFlow_error_msg {}", serialBeanNameMap.getMessage());
            return null;
        }
        TreeWrapper treeWrapper = new TreeWrapper();
        for(Map.Entry<String, List<String>> entry : serialBeanNameMap.getData().entrySet()){
            List<String> value = entry.getValue();
            List<DomainServiceUnit> units = new ArrayList<>();
            for(String name : value){
                DomainServiceUnit bean = getBean(name);
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
                        logicNameMap.put(UUID.randomUUID().toString(), Lists.newArrayList(beanName));
                    }
                }
                recordPoint = currentPoint;
                handleParalle = true;
            }
            //停止 ]
            if(Objects.equals(logicFlow.charAt(currentPoint), CharUtils.toChar("]"))){
                List<String> parallelBean = getParallelBeanName(logicFlow.substring(recordPoint + 1, currentPoint));
                logicNameMap.put(UUID.randomUUID().toString(), parallelBean);
                handleParalle = false;
                recordPoint = currentPoint;
            }
            //常规 找逗号  abd,df,aer,
            if(!handleParalle && Objects.equals(logicFlow.charAt(currentPoint), CharUtils.toChar(","))){
                if(Objects.equals(logicFlow.charAt(recordPoint), CharUtils.toChar("]"))){
                    //说明中间的是一个bean名字
                    String beanName = getNameFromStartToEnd(logicFlow, recordPoint, currentPoint);
                    if(StringUtils.isNotBlank(beanName)){
                        logicNameMap.put(UUID.randomUUID().toString(), Lists.newArrayList(beanName));
                    }
                }
                if(Objects.equals(logicFlow.charAt(recordPoint), CharUtils.toChar(",")) || recordPoint == 0 ){
                    //说明中间的是一个bean名字
                    String beanName = getNameFromStartToEnd(logicFlow, recordPoint, currentPoint);
                    if(StringUtils.isNotBlank(beanName)){
                        logicNameMap.put(UUID.randomUUID().toString(), Lists.newArrayList(beanName));
                    }
                }
                recordPoint = currentPoint;
            }
            //结尾的处理
            if((currentPoint == logicFlow.length() -1) && recordPoint < currentPoint ){
                String beanName = getNameFromStartToEnd(logicFlow, recordPoint, currentPoint + 1);
                if(StringUtils.isNotBlank(beanName)){
                    logicNameMap.put(UUID.randomUUID().toString(), Lists.newArrayList(beanName));
                }
            }
            currentPoint++;
        }

        return FuncRetWrapper.success(logicNameMap);
    }

    private String getNameFromStartToEnd(String logicFlow, int recordPoint, int currentPoint){
        return logicFlow.substring(recordPoint == 0 ? recordPoint : recordPoint + 1, currentPoint );
    }

    private DomainServiceUnit getBean(String name){
        if(StringUtils.isBlank(name)){
            return null;
        }
        boolean match = applicationContext.isTypeMatch(name.trim(), DomainServiceUnit.class);
        if(!match){
            return null;
        }
        return applicationContext.getBean(name.trim(), DomainServiceUnit.class);
    }



    public static void main(String[] args){
        FuncRetWrapper serialBeanNameMap = ExecutorFactory.getInstance(null).getSerialBeanNameMap("ad,d,[123,546,34],234,5345,5");
        System.out.println(serialBeanNameMap.toString());
        serialBeanNameMap = ExecutorFactory.getInstance(null).getSerialBeanNameMap("ad,d[123,546,34]234,5345,5");
        System.out.println(serialBeanNameMap.toString());
    }
}
