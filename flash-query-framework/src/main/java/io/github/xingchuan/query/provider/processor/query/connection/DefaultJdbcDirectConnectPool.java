package io.github.xingchuan.query.provider.processor.query.connection;

import cn.hutool.json.JSONObject;
import io.github.xingchuan.query.api.domain.base.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 直连JDBC
 *
 * @author xingchuan.qxc
 * @date 2022/7/2
 */
public class DefaultJdbcDirectConnectPool extends AbstractConnectionPool {

    public static final String DIRECT_JDBC = "DIRECT_JDBC";

    private Logger logger = LoggerFactory.getLogger(DefaultJdbcDirectConnectPool.class);

    public DefaultJdbcDirectConnectPool(SqlConnectionPoolManager sqlConnectionPoolManager) {
        super(sqlConnectionPoolManager);
    }

    @Override
    public Connection getConnection(DataSource dataSource) throws Exception {
        return fetchDbConnectionWithJdbcDirect(dataSource);
    }

    @Override
    public String poolType() {
        return DIRECT_JDBC;
    }

    /**
     * 通过jdbc直连,获取一个db的connection
     *
     * @param dataSource
     * @return
     * @throws Exception
     */
    private Connection fetchDbConnectionWithJdbcDirect(DataSource dataSource) throws Exception {
        JSONObject properties = dataSource.getProperties();
        String driverClassName = properties.getStr("driverClassName");
        String url = properties.getStr("url");
        String userName = properties.getStr("userName");
        String password = properties.getStr("password");
        Class.forName(driverClassName);
        return DriverManager.getConnection(url, userName, password);
    }
}
