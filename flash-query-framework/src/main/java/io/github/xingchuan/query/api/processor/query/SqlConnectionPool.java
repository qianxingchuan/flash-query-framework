package io.github.xingchuan.query.api.processor.query;

import io.github.xingchuan.query.api.domain.base.DataSource;

import java.sql.Connection;

/**
 * sql的相关连接池实现
 *
 * @author xingchuan.qxc
 * @since 1.0
 */
public interface SqlConnectionPool {

    /**
     * 注册一个数据源
     * @throws Exception
     */
    void registerDataSource() throws Exception;


    /**
     * 获得一个连接
     *
     * @param dataSource 数据源
     * @return 一个连接
     * @throws Exception
     */
    Connection getConnection(DataSource dataSource) throws Exception;


    /**
     * 连接池的类型
     *
     * @return 连接池类型
     */
    String poolType();
}
