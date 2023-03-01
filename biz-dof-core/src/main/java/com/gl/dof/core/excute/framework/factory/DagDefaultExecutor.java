package com.gl.dof.core.excute.framework.factory;

import com.gl.dof.core.excute.framework.content.TreeWrapper;
import com.gl.dof.core.excute.framework.entry.AbstractApplication;
import com.gl.dof.core.excute.framework.executor.LogicExecutor;
import com.gl.dof.core.excute.framework.executor.tree.TreeApplicationExecutor;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class DagDefaultExecutor<REQ, RES> extends AbstractApplication<REQ, RES> {



    @Override
    public LogicExecutor initDoSvrGroup() {

        if (corePoolSize <= 0 || StringUtils.isBlank(executorName)) {
            return new TreeApplicationExecutor(listWrapperP);
        }
        return new TreeApplicationExecutor(listWrapperP, corePoolSize, executorName);
    }

    public DagDefaultExecutor() {
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
}
