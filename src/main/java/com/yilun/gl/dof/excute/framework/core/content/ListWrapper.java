package com.yilun.gl.dof.excute.framework.core.content;

import com.google.common.collect.Lists;
import com.yilun.gl.dof.excute.framework.core.logic.ParrentLogicUnit;
import lombok.Getter;

import java.io.Serializable;
import java.util.*;

/**
 * @Auther: gule.gl
 * @Date: 2022/4/25-04-25-17:31
 * @Description: com.amap.aos.infrastructure.framework.context
 * @version: 1.0
 */
@Getter
public class ListWrapper{

    private LinkedHashMap<String, List<ParrentLogicUnit<? extends ContextData>>> allLogic2 = new LinkedHashMap<>();

    public void add(ParrentLogicUnit<? extends ContextData> unit){
        allLogic2.put(UUID.randomUUID().toString(), Lists.newArrayList(unit));
    }

    public void parallelAdd(ParrentLogicUnit<? extends ContextData>... units){
        List<ParrentLogicUnit<? extends ContextData>> unitList = new ArrayList<>(units.length);
        unitList.addAll(Arrays.asList(units));
        allLogic2.put(UUID.randomUUID().toString(), unitList);
    }
}
