package com.yilun.gl.dof.excute.framework.ddd.repository;

import com.yilun.gl.dof.excute.framework.ddd.exception.RepositoryException;
import com.yilun.gl.dof.excute.framework.ddd.model.Aggregate;
import com.yilun.gl.dof.excute.framework.ddd.repository.model.PageQuery;
import org.springframework.data.domain.Page;

/**
 * 聚合根仓库,定义聚合根常用的存储和查询方法
 * @param <A>
 * @param <Q>
 */
public interface AggregateRepository<A extends Aggregate,Q> extends Repository {

    /**
     * 根据ID 查询
     * @param id id
     * @return 聚合根
     * @throws RepositoryException 异常
     */
    A query(Long id) throws RepositoryException;

    /**
     * 根据查询条件查询
     *
     * @param query 查询条件
     * @return 聚合根
     * @throws RepositoryException 异常
     */
    A query(Q query) throws RepositoryException;

    /**
     * 根据查询条件查询分页
     * @param pageQuery 查询条件
     * @return 聚合根分页集合
     * @throws RepositoryException 异常
     */
    Page<A> queryPage(PageQuery<Q> pageQuery) throws RepositoryException;

    /**
     * 保存
     *
     * @param aggregate 聚合根
     * @throws RepositoryException 异常
     */
    void save(A aggregate) throws RepositoryException;
}
