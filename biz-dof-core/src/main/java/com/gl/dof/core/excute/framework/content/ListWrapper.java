package com.gl.dof.core.excute.framework.content;

import com.gl.dof.core.excute.framework.logic.DomainServiceUnit;

import java.util.LinkedHashMap;
import java.util.List;

public abstract class ListWrapper<T> {

	private T allLogic;

	public T getAllLogic() {
		return allLogic;
	}

	public void setAllLogic(T allLogic){
		this.allLogic = allLogic;
	}
}
