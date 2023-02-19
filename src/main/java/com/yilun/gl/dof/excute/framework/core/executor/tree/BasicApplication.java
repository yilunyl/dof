package com.yilun.gl.dof.excute.framework.core.executor.tree;

import com.yilun.gl.dof.excute.framework.core.DomainServiceGroup;
import com.yilun.gl.dof.excute.framework.core.content.ContextData;
import com.yilun.gl.dof.excute.framework.core.content.TreeWrapper;
import com.yilun.gl.dof.excute.framework.core.logic.DomainServiceUnit;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @description: 存放所有logic；一期，所有logic 顺序添加，串行执行
 * @author: gule
 * @create: 2019-08-18 11:37
 **/
public abstract class BasicApplication< T extends ContextData> extends DomainServiceGroup<T> {

    private LinkedHashMap<String, List<DomainServiceUnit<? extends ContextData>>> allLogic = new LinkedHashMap<>();

    void init(){
        TreeWrapper listWrapper = new TreeWrapper();
        init(listWrapper);
        allLogic = listWrapper.getAllLogic2();
    }
    /**
     * 获取执行单元组中的所有logic
     *
     * @return
     */
    protected LinkedHashMap<String, List<DomainServiceUnit<? extends ContextData>>> getAllLogic(){
        return allLogic;
    }
}
