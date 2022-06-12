package com.yilun.gl.dof.excute.framework.config.dynconfig;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author gl
 * @Auther: gl
 * @Date: 2020/10/22 15:48
 * @Description: 使用示例
 * @DofDyncValue(project = "1234.567", path = "degrade.search_es.flag")
 * private Boolean searchEsFlag;
 * @DofDyncValue(project = "123123.123123", path = "ab_test.5_4_0_0.city_with_time", innerClass = CityWithTime.class)
 * private List<CityWithTime> cityWithTime;
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DofDyncValue {

    /**
     * 项目名字表达式:"123123.123123" 或者 "312313.12312313"
     *
     * @return String
     */
    String project();

    /**
     * 具体路径的表达式:"systemProperties.myProp"
     *
     * @return int
     */
    String path();

    /**
     * 动态数据中心来源, 默认是zk，其他暂不支持
     * 参考 @link{ DyncConfigTypeEnum}
     *
     * @return int
     */
    int dyncType() default 0;

    /**
     * 当有list对象的时候需要给出内部类的class
     *
     * @return Class
     */
    Class<?>[] innerClass() default {};
}
