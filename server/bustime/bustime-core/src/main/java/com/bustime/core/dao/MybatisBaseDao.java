package com.bustime.core.dao;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;

/**
 * dao基类.
 *
 * @author chengdong
 */
public class MybatisBaseDao<T> extends SqlSessionDaoSupport {

    private static final String NAMESPACE = "bustime.";
    private static final String SAVE_PREFIX = "save";
    private static final String UPDATE_PREFIX = "update";

    /**
     * 查询多个数据对象.
     * @param statement sql id
     * @param parameter 参数对象
     * @return 对象集合
     */
    @SuppressWarnings("unchecked")
    public List<T> selectList(String statement, Object parameter) {
        return this.getSqlSession().selectList(NAMESPACE + statement, parameter);
    }

    @SuppressWarnings("unchecked")
    public T selectOne(String statement, Object parameter) {
        return (T) this.getSqlSession().selectOne(NAMESPACE + statement, parameter);
    }

    public void save(String statement, Object parameter) {
        this.getSqlSession().insert(NAMESPACE + statement, parameter);
    }

    public int update(String statement, Object parameter) {
        return this.getSqlSession().update(NAMESPACE + statement, parameter);
    }

    public int delete(String statement, Object parameter) {
        return this.getSqlSession().delete(NAMESPACE + statement, parameter);
    }

    public void saveOrUpdate(String statement, Object parameters) {

        int updateCount = this.getSqlSession().update(NAMESPACE + UPDATE_PREFIX + statement, parameters);
        if (updateCount == 0) {
            this.getSqlSession().insert(NAMESPACE + SAVE_PREFIX + statement, parameters);
        }

    }

}
