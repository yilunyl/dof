package com.yilun.gl.dof.excute.framework.core.content;

import com.google.common.collect.Lists;
import com.yilun.gl.dof.excute.framework.core.context.HandleContext;
import com.yilun.gl.dof.excute.framework.core.logic.DomainServiceUnit;

import java.util.*;

/**
 * @Auther: gule.gl
 * @Date: 2022/4/25-04-25-17:31
 * @Description: com.amap.aos.infrastructure.framework.context
 * @version: 1.0
 */
public class TreeWrapper {
    private LinkedHashMap<String, List<DomainServiceUnit>> allLogic2 = new LinkedHashMap<>();

    public void add(DomainServiceUnit unit){
        allLogic2.put(UUID.randomUUID().toString(), Lists.newArrayList(unit));
    }

    @SafeVarargs
    public final void parallelAdd(DomainServiceUnit... units){
        List<DomainServiceUnit> unitList = new ArrayList<>(units.length);
        unitList.addAll(Arrays.asList(units));
        allLogic2.put(UUID.randomUUID().toString(), unitList);
    }

    public LinkedHashMap<String, List<DomainServiceUnit>> getAllLogic2() {
        return allLogic2;
    }
}
