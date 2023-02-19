package com.yilun.gl.dof.excute.framework.core.entry;

import com.yilun.gl.dof.excute.framework.core.LogicExecutor;
import com.yilun.gl.dof.excute.framework.core.common.LogicResult;
import com.yilun.gl.dof.excute.framework.core.content.ContextData;
import org.apache.dubbo.config.spring.extension.SpringExtensionFactory;
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
public abstract  class AbstractApplication<T extends ContextData, REQ, RES> implements ApplicationInit<T, REQ, RES>, InitializingBean, ApplicationContextAware , BeanNameAware , ApplicationListener<ContextRefreshedEvent> {

	private transient LogicExecutor<T> logicExecutor;

	private transient ApplicationContext applicationContext;

	private transient String beanName;

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
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}


	@Override
	public RES doLogicSchedule(REQ req,  Object... others ) {
		//初始化参数
		T t = initContext(req, others);
		//执行编排逻辑
		LogicResult logicResult = logicExecutor.doLogicSchedule(t);
		//构建返回值
		return buildResponse(logicResult, t, req, others);

	}

	/**
	 * 初始化上下文
	 * @param req
	 * @return
	 */
	protected abstract T initContext(REQ req, Object... others);

	/**
	 * 构建返回参数
	 * @param logicResult
	 * @param t
	 * @param req
	 * @return
	 */
	protected abstract RES buildResponse(LogicResult logicResult, T t, REQ req, Object... others);

}
