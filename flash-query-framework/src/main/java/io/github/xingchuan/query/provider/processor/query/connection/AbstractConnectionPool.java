package io.github.xingchuan.query.provider.processor.query.connection;

import io.github.xingchuan.query.api.processor.query.SqlConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xingchuan.qxc
 * @since 1.0
 */
public abstract class AbstractConnectionPool implements SqlConnectionPool {

    private Logger logger = LoggerFactory.getLogger(AbstractConnectionPool.class);

    private SqlConnectionPoolManager sqlConnectionPoolManager;

    public AbstractConnectionPool(SqlConnectionPoolManager sqlConnectionPoolManager) {
        this.sqlConnectionPoolManager = sqlConnectionPoolManager;
    }


    @Override
    public void registerDataSource() throws Exception {
        this.sqlConnectionPoolManager.registerSqlConnectionPool(this);
    }
}
