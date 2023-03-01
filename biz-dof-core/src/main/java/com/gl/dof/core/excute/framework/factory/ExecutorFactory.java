package com.gl.dof.core.excute.framework.factory;

import com.gl.dof.core.excute.framework.annotation.DofReference;
import com.gl.dof.core.excute.framework.content.DagWrapper;
import com.gl.dof.core.excute.framework.content.TreeWrapper;
import com.gl.dof.core.excute.framework.entry.AbstractApplication;
import com.gl.dof.core.excute.framework.executor.ExecutorType;
import com.gl.dof.core.excute.framework.executor.dag.DagAnalysisLogicFlow;
import com.gl.dof.core.excute.framework.executor.tree.TreeAnalysisLogicFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import java.util.Objects;


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
            TreeWrapper treeWrapper1 = TreeAnalysisLogicFlow.analysisLogicFlow(annotation.logicFlow(), applicationContext);
            abstractApplication.setTreeWrapperI(treeWrapper1);
        }else{
            //ToDo 图的链需要单独解析
            DagWrapper dagWrapper1 = DagAnalysisLogicFlow.analysisLogicFlow(annotation.logicFlow(), applicationContext);
            abstractApplication = new DagDefaultExecutor();
            abstractApplication.setTreeWrapperI(dagWrapper1);

        }
        if(annotation.corePoolSize() > 1){
            abstractApplication.setCorePoolSize(annotation.corePoolSize());
        }
        //设置线程池
        if(!annotation.useCommonPool()){
            abstractApplication.setExecutorName(annotation.funcKey());
        }
        //执行
        abstractApplication.concreteExecuteBeanInit();

        if(!abstractApplication.getInitSuccess()){
            logger.error("ExecutorFactory abstractApplication_init_fail 执行器链构建失败");
            return null;
        }
        return abstractApplication;
    }
}
