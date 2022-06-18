package com.yilun.gl.dof.excute.framework.core;


import com.yilun.gl.dof.excute.framework.core.content.ContextData;
import com.yilun.gl.dof.excute.framework.core.content.ListWrapper;

/**
 * @ClassName InitLogicGroup
 * @Author gule.gl
 * @Date 2021/6/19 1:00 下午
 * @Description InitLogicGroup
 * @Version 1.0
 */
public interface InitLogicGroup<T extends ContextData> {


    void dataFrontProcessor(ListWrapper listWrapper);

    void dataProcessor(ListWrapper listWrapper);

    void dataPostProcessor(ListWrapper listWrapper);

}
