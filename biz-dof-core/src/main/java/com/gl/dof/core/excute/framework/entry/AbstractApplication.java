package com.gl.dof.core.excute.framework.entry;

import com.gl.dof.core.excute.framework.content.ListWrapper;
import com.gl.dof.core.excute.framework.content.TreeWrapper;
import com.gl.dof.core.excute.framework.executor.LogicExecutor;
import com.gl.dof.core.excute.framework.common.LogicResult;
import com.gl.dof.core.excute.framework.context.DefaultHandleContext;
import com.gl.dof.core.excute.framework.context.HandleContext;
import com.gl.dof.core.excute.framework.exception.DofResCode;
import com.gl.dof.core.excute.framework.exception.DofServiceException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;

/**
 * @ClassName: biz-dof AbstructApplication
 * @Description: com.gl.dof.core.excute.framework.entry
 * @Author: 逸伦
 * @Date: 2023/2/18 23:17
 * @Version: 1.0
 */
public abstract  class AbstractApplication<REQ, RES> implements ApplicationInit<REQ, RES>{

	private final static Logger log = LoggerFactory.getLogger(AbstractApplication.class);

	protected transient LogicExecutor logicExecutor;

	protected ListWrapper listWrapperP;

	protected int corePoolSize;
	protected String executorName;

	private Boolean initSuccess = Boolean.FALSE;
	private HandleContext newCtx;

	public void concreteExecuteBeanInit(String funcKey){

		if(!initSuccess){
			logicExecutor = this.initDoSvrGroup();
			newCtx = new DefaultHandleContext(funcKey, this.getClass().getName());
			log.info("Dof-logicExecutor_init_success id={}|name={}",newCtx.getHandleContextId(), newCtx.getHandleContextName());
			initSuccess = Boolean.TRUE;
		}
	}


	@Override
	public RES doLogicSchedule(Input<REQ> input, Output<RES> output ){

		try{
			if(!initSuccess){
				throw DofServiceException.build(DofResCode.FAILE, "初始化失败无法执行逻辑");
			}
			//初始化参数
			input.doIn(newCtx);
			//执行编排逻辑
			LogicResult logicResult = logicExecutor.doLogicSchedule(newCtx);
			//构建返回值
			return output.doOut(newCtx, logicResult);
		}finally{
			//强制清空上下文
			newCtx.clear();
		}
	}

	public void setTreeWrapperI(ListWrapper treeWrapperI) {
		this.listWrapperP = treeWrapperI;
	}

	public abstract void setCorePoolSize(int corePoolSize) ;

	public abstract void setExecutorName(String executorName);

	public Boolean getInitSuccess() {
		return initSuccess;
	}
}
