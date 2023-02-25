package com.yilun.gl.dof.excute.framework.core.entry;

import com.yilun.gl.dof.excute.framework.config.dynconfig.core.BasicDyncConfigService;
import com.yilun.gl.dof.excute.framework.core.LogicExecutor;
import com.yilun.gl.dof.excute.framework.core.common.LogicResult;
import com.yilun.gl.dof.excute.framework.core.context.DefaultHandleContext;
import com.yilun.gl.dof.excute.framework.core.context.HandleContext;
import org.apache.dubbo.config.spring.extension.SpringExtensionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @ClassName: biz-dof AbstructApplication
 * @Description: com.yilun.gl.dof.excute.framework.core.entry
 * @Author: 逸伦
 * @Date: 2023/2/18 23:17
 * @Version: 1.0
 */
public abstract  class AbstractApplication<REQ, RES> implements ApplicationInit<REQ, RES>, InitializingBean, ApplicationContextAware , BeanNameAware , ApplicationListener<ContextRefreshedEvent> {

	private final static Logger log = LoggerFactory.getLogger(AbstractApplication.class);

	private transient LogicExecutor logicExecutor;

	private transient ApplicationContext applicationContext;

	private transient String beanName;

	private HandleContext newCtx;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
		SpringExtensionFactory.addApplicationContext(applicationContext);
	}

	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event){
		logicExecutor = this.initDoSvrGroup();
		newCtx = new DefaultHandleContext(this.beanName, this.getClass().getName());
		log.info("logicExecutor_init_success id={}|name={}",newCtx.getHandleContextId(), newCtx.getHandleContextName());
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}


	@Override
	public RES doLogicSchedule(REQ req,  Object... others ) {
		//初始化参数
		initContext(newCtx, req, others);
		//执行编排逻辑
		LogicResult logicResult = logicExecutor.doLogicSchedule(newCtx);
		//构建返回值
		return buildResponse(logicResult, newCtx);

	}

	/**
	 * 初始化上下文
	 * @param req
	 * @return
	 */
	protected abstract void initContext(HandleContext ctx, REQ req, Object... others);

	/**
	 * 构建返回参数
	 * @param logicResult
	 * @param ctx
	 * @param req
	 * @return
	 */
	protected abstract RES buildResponse(LogicResult logicResult, HandleContext ctx);

}
