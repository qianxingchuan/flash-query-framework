package io.github.xingchuan.query.provider.processor.query;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.github.xingchuan.query.api.domain.base.DataSource;
import io.github.xingchuan.query.api.domain.enums.DataSourceType;
import io.github.xingchuan.query.base.PrepareData;
import io.github.xingchuan.query.provider.processor.query.connection.DefaultJdbcDirectConnectPool;
import io.github.xingchuan.query.provider.processor.query.connection.SqlConnectionPoolManager;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * @author xingchuan.qxc
 * @since 1.0
 */
public class SqlDataQueryProcessorTest {

    private Logger logger = LoggerFactory.getLogger(SqlDataQueryProcessorTest.class);

    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final String DRIVER_CLASS = "org.h2.Driver";
    private String JDBC_URL = "jdbc:h2:tcp://localhost:9092/mem:sample_db";


    @Before
    public void prepareData() throws Exception {
        List<String> sqlList = FileUtil.readUtf8Lines(FileUtil.file("prepared_sql_data.sql"));
        PrepareData prepareData = new PrepareData();
        prepareData.startServer();
        prepareData.preparedData(sqlList);
    }


    @Test
    public void test_从关系型数据库查询数据() throws Exception {
        SqlConnectionPoolManager sqlConnectionPoolManager = new SqlConnectionPoolManager();
        SqlDataQueryProcessor sqlDataQueryProcessor = new SqlDataQueryProcessor(sqlConnectionPoolManager);
        sqlConnectionPoolManager.registerSqlConnectionPool(new DefaultJdbcDirectConnectPool(sqlConnectionPoolManager));

        JSONObject dbProperties = JSONUtil.createObj();
        dbProperties.set("driverClassName", DRIVER_CLASS);
        dbProperties.set("url", JDBC_URL);
        dbProperties.set("userName", USER);
        dbProperties.set("password", PASSWORD);

        DataSource dataSource = new DataSource();
        dataSource.setDataSourceType(DataSourceType.DB);
        dataSource.setCode("sample_db");
        dataSource.setProperties(dbProperties);
        JSONArray resultArray = sqlDataQueryProcessor.queryData("select * from USER_INF ", dataSource);
        logger.info("{}", resultArray.toStringPretty());

        assertThat(resultArray).isNotEmpty();
    }
}