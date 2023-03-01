package com.yilun.gl.dof.excute.framework.starter;

import com.gl.dof.core.excute.framework.annotation.DofReferenceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: biz-dof DofAutoConfiguration
 * @Description: com.yilun.gl.dof.excute.framework.starter
 * @Author: 逸伦
 * @Date: 2023/2/28 09:19
 * @Version: 1.0
 */
@Configuration
@ConditionalOnClass
public class DofAutoConfiguration {

	private final static Logger logger = LoggerFactory.getLogger(DofAutoConfiguration.class);

	static {
		logger.info("DofAutoConfiguration init..");
	}

	@Bean
	@ConditionalOnMissingBean
	public DofReferenceBean dofReferenceBean(){
		return new DofReferenceBean();
	}
}
