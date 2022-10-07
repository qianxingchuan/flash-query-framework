package io.github.xingchuan.query.provider.processor.senario;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import io.github.xingchuan.query.api.domain.base.DataQueryResponse;
import io.github.xingchuan.query.api.domain.base.DataSource;
import io.github.xingchuan.query.api.domain.base.QueryField;
import io.github.xingchuan.query.api.domain.enums.CacheType;
import io.github.xingchuan.query.api.domain.enums.ConvertorType;
import io.github.xingchuan.query.api.domain.enums.DataSourceType;
import io.github.xingchuan.query.api.domain.senario.DataQueryScenario;
import io.github.xingchuan.query.api.domain.senario.cache.CacheConfig;
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
import org.mockito.Mockito;
import org.mockito.internal.util.collections.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static io.github.xingchuan.sql.provider.impl.DefaultMybatisSqlParseProvider.MYBATIS_SQL_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    SqlDataQueryProcessor sqlDataQueryProcessorForTestCase = null;

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

        // 注册spy的这个bean，便于后面来进行测试用例测试
        sqlDataQueryProcessorForTestCase = Mockito.spy(new SqlDataQueryProcessor(sqlConnectionPoolManager, dataQueryProcessorManager));
        dataQueryProcessorManager.registerDataQueryProcessor(sqlDataQueryProcessorForTestCase);

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
        JSON data = response.getData();
        assertThat(data).isNotNull();


        response = dataQueryScenarioProcessor.process(scenario, params);
        logger.info("{}", response);
        data = response.getData();
        assertThat(data).isNotNull();

        verify(sqlDataQueryProcessorForTestCase, times(2)).queryData(any(), any());
    }


    /**
     * 场景2： 有缓存，只要参数匹配，就会通过缓存来返回数据
     *
     * @throws Exception
     */
    @Test
    public void test_cache_scenario() throws Exception {
        DataQueryScenario scenario = buildCacheScenario();
        JSONObject params = JSONUtil.createObj();
        params.set("id", 1L);
        DataQueryResponse<JSON> response = dataQueryScenarioProcessor.process(scenario, params);
        logger.info("{}", response);
        JSON data = response.getData();
        assertThat(data).isNotNull();

        response = dataQueryScenarioProcessor.process(scenario, params);
        logger.info("{}", response);
        data = response.getData();
        assertThat(data).isNotNull();

        verify(sqlDataQueryProcessorForTestCase, times(1)).queryData(any(), any());
    }

    @Test
    public void test_DesensitizeField_ok() throws Exception {
        DataQueryScenario scenario = buildDefaultScenario();
        List<QueryField> outputFields = new ArrayList<>();
        QueryField field = new QueryField();
        field.setFieldName("NAME");
        field.setFieldConvertTypes(Sets.newSet(ConvertorType.DesensitizedType.name()));
        outputFields.add(field);
        scenario.setOutputFields(outputFields);
        logger.info("{}", JSONUtil.parseObj(scenario));
        JSONObject params = JSONUtil.createObj();
        params.set("id", 1L);
        DataQueryResponse<JSON> response = dataQueryScenarioProcessor.process(scenario, params);
        logger.info("{}", response);
        JSON data = response.getData();
        assertThat(data).isNotNull();
        JSONArray array = (JSONArray) data;
        JSONObject result = array.getJSONObject(0);
        assertThat(result.getStr("NAME")).isEqualTo("*************");
    }

    private DataQueryScenario buildCacheScenario() {
        DataQueryScenario scenario = buildDefaultScenario();
        CacheConfig cacheConfig = new CacheConfig();
        cacheConfig.setCacheType(CacheType.LOCAL_MEMORY.name());
        cacheConfig.setCode("local_memory_cache");
        cacheConfig.setProperties(JSONUtil.createObj());
        scenario.setCacheConfig(cacheConfig);
        logger.info("{}", JSONUtil.parseObj(scenario));
        return scenario;
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

        logger.info("{}", JSONUtil.parseObj(scenario));
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