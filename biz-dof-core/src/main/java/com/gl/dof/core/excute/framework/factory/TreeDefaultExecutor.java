package com.gl.dof.core.excute.framework.factory;

import com.gl.dof.core.excute.framework.content.ListWrapper;
import com.gl.dof.core.excute.framework.executor.LogicExecutor;
import com.gl.dof.core.excute.framework.content.TreeWrapper;
import com.gl.dof.core.excute.framework.entry.AbstractApplication;
import com.gl.dof.core.excute.framework.executor.tree.BasicTreeApplication;
import com.gl.dof.core.excute.framework.executor.tree.TreeApplicationExecutor;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class TreeDefaultExecutor<REQ, RES> extends AbstractApplication<REQ, RES> {


    @Override
    public LogicExecutor initDoSvrGroup() {

        if (super.corePoolSize <= 0 || StringUtils.isBlank(super.executorName)) {
            return new TreeApplicationExecutor(new BasicTreeApplication() {
                @Override
                protected void init(TreeWrapper listWrapper) {
                    listWrapper.setAllLogic(listWrapperP.getAllLogic());
                }
            });
        }
        return new TreeApplicationExecutor(new BasicTreeApplication() {
            @Override
            protected void init(TreeWrapper listWrapper) {
                listWrapper.setAllLogic(listWrapperP.getAllLogic());
            }
        }, corePoolSize, executorName);
    }

    public TreeDefaultExecutor() {
    }

    public void setTreeWrapperI(TreeWrapper treeWrapperI) {
        this.listWrapperP = treeWrapperI;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    @Override
    public boolean initSuccess() {
        if(Objects.isNull(logicExecutor) || Objects.isNull(listWrapperP)){
            return false;
        }
        return true;
    }
}