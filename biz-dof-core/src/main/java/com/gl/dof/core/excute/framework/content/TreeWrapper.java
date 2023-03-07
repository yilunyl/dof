package com.gl.dof.core.excute.framework.content;

import com.google.common.collect.Lists;
import com.gl.dof.core.excute.framework.logic.LogicUnit;

import java.util.*;

/**
 * @Auther: gule.gl
 * @Date: 2022/4/25-04-25-17:31
 * @Description: com.amap.aos.infrastructure.framework.context
 * @version: 1.0
 */
public class TreeWrapper extends ListWrapper{
    private LinkedHashMap<String, List<LogicUnit>> allLogic = new LinkedHashMap<>();

    public void add(LogicUnit unit){
        allLogic.put(UUID.randomUUID().toString(), Lists.newArrayList(unit));
    }


    public final void parallelAdd(List<LogicUnit> unitList){
        allLogic.put(UUID.randomUUID().toString(), unitList);
    }

    public LinkedHashMap<String, List<LogicUnit>> getAllLogic() {
        return allLogic;
    }

    public void setAllLogic(LinkedHashMap<String, List<LogicUnit>> allLogic) {
        this.allLogic = allLogic;
    }
}
