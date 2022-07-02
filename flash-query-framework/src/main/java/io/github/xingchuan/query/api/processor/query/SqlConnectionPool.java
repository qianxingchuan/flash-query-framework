package io.github.xingchuan.query.api.processor.query;

import io.github.xingchuan.query.api.domain.base.DataSource;

import java.sql.Connection;

/**
 * sql的相关连接池实现
 *
 * @author xingchuan.qxc
 * @date 2022/6/12
 */
public interface SqlConnectionPool {

    /**
     * 注册一个数据源
     *
     * @param dataSource
     */
    void registerDataSource() throws Exception;


    /**
     * 获得一个连接
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    Connection getConnection(DataSource dataSource) throws Exception;


    /**
     * 连接池的类型
     *
     * @return
     */
    String poolType();
}
