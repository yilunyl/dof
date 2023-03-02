package com.gl.dof.core.excute.framework.content;

public abstract class ListWrapper<T> {

	private T allLogic;

	public T getAllLogic() {
		return allLogic;
	}

	public void setAllLogic(T allLogic){
		this.allLogic = allLogic;
	}
}
