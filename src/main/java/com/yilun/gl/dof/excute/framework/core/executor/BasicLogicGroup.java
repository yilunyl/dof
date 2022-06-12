package com.yilun.gl.dof.excute.framework.core.executor;

import com.yilun.gl.dof.excute.framework.core.content.ContextData;
import com.yilun.gl.dof.excute.framework.core.content.ListWrapper;
import com.yilun.gl.dof.excute.framework.core.logic.ParrentLogicUnit;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @description: 存放所有logic；一期，所有logic 顺序添加，串行执行
 * @author: gule
 * @create: 2019-08-18 11:37
 **/
public abstract class BasicLogicGroup< T extends ContextData> implements InitLogicGroup<T> {

    private LinkedHashMap<String, List<ParrentLogicUnit<? extends ContextData>>> allLogic = new LinkedHashMap<>();

    void init(){
        ListWrapper listWrapper = new ListWrapper();
        dataFrontProcessor(listWrapper);
        dataProcessor(listWrapper);
        dataPostProcessor(listWrapper);

        allLogic = listWrapper.getAllLogic2();
    }
    /**
     * 获取执行单元组中的所有logic
     *
     * @return
     */
    protected LinkedHashMap<String, List<ParrentLogicUnit<? extends ContextData>>> getAllLogic(){
        return allLogic;
    }
}
