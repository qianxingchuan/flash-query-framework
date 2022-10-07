package io.github.xingchuan.query.provider.processor.senario;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import io.github.xingchuan.query.api.domain.base.DataQueryResponse;
import io.github.xingchuan.query.api.domain.base.DataSource;
import io.github.xingchuan.query.api.domain.enums.DataSourceType;
import io.github.xingchuan.query.api.domain.senario.DataQueryScenario;
import io.github.xingchuan.query.api.processor.convertor.FieldValueConvertor;
import io.github.xingchuan.query.api.processor.senario.DataQueryScenarioProcessor;
import io.github.xingchuan.query.base.PrepareData;
import io.github.xingchuan.query.provider.cache.CacheManager;
import io.github.xingchuan.query.provider.cache.FileDataCacheProcessor;
import io.github.xingchuan.query.provider.cache.LocalMemoryCacheProcessor;
import io.github.xingchuan.query.provider.processor.convertor.DesensitizeFieldValueConvertor;
import io.github.xingchuan.query.provider.processor.query.DataQueryProcessorManager;
import io.github.xingchuan.query.provider.processor.query.SqlDataQueryProcessor;
import io.github.xingchuan.query.provider.processor.query.connection.DefaultJdbcDirectConnectPool;
import io.github.xingchuan.query.provider.processor.query.connection.SqlConnectionPoolManager;
import io.github.xingchuan.sql.engine.FlashSqlEngine;
import io.github.xingchuan.sql.provider.impl.DefaultMybatisSqlParseProvider;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static io.github.xingchuan.sql.provider.impl.DefaultMybatisSqlParseProvider.MYBATIS_SQL_TYPE;

/**
 * 场景相关配置
 *
 * @author xingchuan.qxc
 * @since
 */
public class DataQueryScenarioProcessorImplTest {

    private Logger logger = LoggerFactory.getLogger(DataQueryScenarioProcessorImplTest.class);


    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static final String DRIVER_CLASS = "org.h2.Driver";
    private String JDBC_URL = "jdbc:h2:tcp://localhost:9092/mem:sample_db";

    private DataQueryScenarioProcessor dataQueryScenarioProcessor = null;

    @Before
    public void prepareData() throws Exception {
        List<String> sqlList = FileUtil.readUtf8Lines(FileUtil.file("prepared_sql_data.sql"));
        PrepareData prepareData = new PrepareData();
        prepareData.startServer();
        prepareData.preparedData(sqlList);

        // 初始化cacheManager
        CacheManager cacheManager = new CacheManager();
        // 会自动注册到cacheManger
        new FileDataCacheProcessor(cacheManager);
        // 会自动注册到cacheManager
        new LocalMemoryCacheProcessor(cacheManager);

        //初始化DataQueryProcessorManager
        DataQueryProcessorManager dataQueryProcessorManager = new DataQueryProcessorManager();
        //
        SqlConnectionPoolManager sqlConnectionPoolManager = new SqlConnectionPoolManager();
        //自动注册到sql连接管理
        new DefaultJdbcDirectConnectPool(sqlConnectionPoolManager);
        new SqlDataQueryProcessor(sqlConnectionPoolManager, dataQueryProcessorManager);

        // 初始化SqlEngine
        FlashSqlEngine sqlEngine = new FlashSqlEngine();
        sqlEngine.loadConfig("test-sql-id-mapper.xml");
        sqlEngine.registerSqlParseProvider(MYBATIS_SQL_TYPE, new DefaultMybatisSqlParseProvider());
        //初始化系统的字段转换器
        List<FieldValueConvertor> desensitizeFieldValueConvertors = Lists.newArrayList(new DesensitizeFieldValueConvertor());
        dataQueryScenarioProcessor = new DataQueryScenarioProcessorImpl(cacheManager, dataQueryProcessorManager, sqlEngine, desensitizeFieldValueConvertors);
    }

    /**
     * 场景1： 没有缓存，每次都是直接查询
     */
    @Test
    public void test_default_scenario() throws Exception {
        DataQueryScenario scenario = buildDefaultScenario();
        JSONObject params = JSONUtil.createObj();
        params.set("id", 1L);
        DataQueryResponse<JSON> response = dataQueryScenarioProcessor.process(scenario, params);
        logger.info("{}", response);
    }

    private DataQueryScenario buildDefaultScenario() {
        DataQueryScenario scenario = new DataQueryScenario();
        scenario.setCode("default-test-scenario");
        scenario.setDataSource(buildDataSource());
        scenario.setName("测试场景1");
        scenario.setCacheConfig(null);
        scenario.setInputFields(null);
        scenario.setOutputFields(null);
        scenario.setTemplateProviderType(MYBATIS_SQL_TYPE);
        scenario.setSqlId("getUserById");
        return scenario;
    }

    private DataSource buildDataSource() {
        JSONObject dbProperties = JSONUtil.createObj();
        dbProperties.set("driverClassName", DRIVER_CLASS);
        dbProperties.set("url", JDBC_URL);
        dbProperties.set("userName", USER);
        dbProperties.set("password", PASSWORD);

        DataSource dataSource = new DataSource();
        dataSource.setDataSourceType(DataSourceType.DB);
        dataSource.setCode("sample_db");
        dataSource.setProperties(dbProperties);
        return dataSource;
    }
}