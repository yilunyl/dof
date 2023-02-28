package com.gl.dof.core.excute.framework.executor.dag;

import com.gl.dof.core.excute.framework.content.DagWrapper;
import com.gl.dof.core.excute.framework.executor.dag.DomainServiceGroup;
import com.gl.dof.core.excute.framework.logic.DomainServiceUnit;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @description: 需要在开发
 * @author: gule
 * @create: 2019-08-18 11:37
 **/
public abstract class BasicDagApplication extends DomainServiceGroup {

    private LinkedHashMap<String, List<DomainServiceUnit>> allLogic = new LinkedHashMap<>();

    void init(){
        DagWrapper listWrapper = new DagWrapper();
        init(listWrapper);
        allLogic = (LinkedHashMap<String, List<DomainServiceUnit>>) listWrapper.getAllLogic();
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
