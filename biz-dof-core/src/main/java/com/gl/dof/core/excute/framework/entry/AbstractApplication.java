package com.gl.dof.core.excute.framework.entry;

import com.gl.dof.core.excute.framework.content.ListWrapper;
import com.gl.dof.core.excute.framework.content.TreeWrapper;
import com.gl.dof.core.excute.framework.executor.LogicExecutor;
import com.gl.dof.core.excute.framework.common.LogicResult;
import com.gl.dof.core.excute.framework.context.DefaultHandleContext;
import com.gl.dof.core.excute.framework.context.HandleContext;
import com.gl.dof.core.excute.framework.exception.DofResCode;
import com.gl.dof.core.excute.framework.exception.DofServiceException;
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
public abstract  class AbstractApplication<REQ, RES> implements ApplicationInit<REQ, RES>, InitializingBean , BeanNameAware , ApplicationListener<ApplicationContextEvent>{

	private final static Logger log = LoggerFactory.getLogger(AbstractApplication.class);

	protected transient LogicExecutor logicExecutor;

	private transient String beanName;

	protected ListWrapper listWrapperP;

	protected int corePoolSize;
	protected String executorName;

	private Boolean initSuccess = Boolean.FALSE;
	private Boolean isStop = Boolean.FALSE;
	private HandleContext newCtx;

	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}

	@Override
	public void onApplicationEvent(ApplicationContextEvent event){

		if(!initSuccess && (event instanceof ContextRefreshedEvent)){
			logicExecutor = this.initDoSvrGroup();
			newCtx = new DefaultHandleContext(this.beanName, this.getClass().getName());
			log.info("logicExecutor_init_success id={}|name={}",newCtx.getHandleContextId(), newCtx.getHandleContextName());
			initSuccess = Boolean.TRUE;
		}
		if(event instanceof ContextClosedEvent || event instanceof ContextStoppedEvent){
			if(!isStop){
				newCtx.clear();
				isStop = Boolean.TRUE;
				log.info("logicExecutor_destroy_success id={}|name={}",newCtx.getHandleContextId(), newCtx.getHandleContextName());
				initSuccess = Boolean.FALSE;
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}


	@Override
	public RES doLogicSchedule(Input<REQ> input, Output<RES> output ){

		try{
			if(!initSuccess || isStop){
				log.info("logicExecutor_init_success id={}|name={}",newCtx.getHandleContextId(), newCtx.getHandleContextName());
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

	public abstract void setTreeWrapperI(TreeWrapper treeWrapperI);

	public abstract void setCorePoolSize(int corePoolSize) ;

	public abstract void setExecutorName(String executorName);

	public abstract boolean initSuccess();

}
