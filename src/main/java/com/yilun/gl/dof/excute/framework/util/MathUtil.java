package com.yilun.gl.dof.excute.framework.util;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @ClassName: biz-dof MathUtil
 * @Description: com.yilun.gl.dof.excute.framework.util
 * @Author: 逸伦
 * @Date: 2023/2/26 17:57
 * @Version: 1.0
 */
public class MathUtil {

	public static String divide(int divide,  int divided, int i) {
		if(divided == 0){
			divided =  1;
		}
		return BigDecimal.valueOf(divide).divide(BigDecimal.valueOf(divided),i, RoundingMode.HALF_DOWN).toString();
	}

	private MathUtil() {
	}
}
