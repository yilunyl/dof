package com.gl.dof.core.excute.framework.executor.tree;

import com.gl.dof.core.excute.framework.content.TreeWrapper;
import com.gl.dof.core.excute.framework.logic.DomainServiceUnit;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @description: 存放所有logic；一期，所有logic 顺序添加，串行执行
 * @author: gule
 * @create: 2019-08-18 11:37
 **/
public abstract class BasicTreeApplication extends DomainServiceGroup {

    private LinkedHashMap<String, List<DomainServiceUnit>> allLogic = new LinkedHashMap<>();

    void init(){
        TreeWrapper listWrapper = new TreeWrapper();
        init(listWrapper);
        allLogic = listWrapper.getAllLogic();
    }
    /**
     * 获取执行单元组中的所有logic
     *
     * @return
     */
    protected LinkedHashMap<String, List<DomainServiceUnit>> getAllLogic(){
        return allLogic;
    }
}
