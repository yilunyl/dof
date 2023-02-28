package com.gl.dof.core.excute.framework.ddd.repository.model;

public class PageQuery<Q> {

    private int startIndex = 0;

    private int pageSize = 10;

    private Q query;

    public PageQuery(){

    }

    public int getStartIndex() {
        return startIndex;
    }

    public PageQuery<Q> setStartIndex(int startIndex) {
        this.startIndex = startIndex;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public PageQuery<Q> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public Q getQuery() {
        return query;
    }

    public PageQuery<Q> setQuery(Q query) {
        this.query = query;
        return this;
    }

    public static <Q> PageQuery<Q> build(Q query){
        PageQuery<Q>
            pageQuery = new PageQuery<>();
        pageQuery.query = query;
        return pageQuery;
    }
}
