# flash-query-framework

# Background

在2019年的时候，由于人力成本等因素，项目组没有前端人员，所以我们需要自己开发报表（react + fusion + 后端接口），
开始两周，大家都在重复同样的工作( 写SQL -> 写接口 -> 写页面)。

我希望有更多的时间做自己的事情，于是我尝试把这个过程进行抽象，可以自动的生成我们需要的报表页面。

最终，我完成了一个FlashQuery的框架，这个框架只需要写Sql模板（可以任意模板，但是为了降低学习成本，默认支持MyBatis的xml模板格式），模板中的参数就是前端的入参，整个过程抽象成了
（前端传参 -> 接收到JsonObject -> 渲染sql -> 执行sql -> 返回前端组件认识的格式 -> 报表展现)

FlashSqlEngine 是一个模板渲染引擎，详情参照 [FlashSqlEngine](https://github.com/flash-query/FlashSqlEngine)

# How to use?

## maven 依赖

```
         <dependency>
            <groupId>io.github.flash-query</groupId>
            <artifactId>flash-query-framework</artifactId>
            <version>1.0</version>
        </dependency>

```

## Quick Start

### 1、 初始化CacheManager(缓存)

```
    
    CacheManager cacheManager = new CacheManager();
    // 会自动注册到cacheManger
    new FileDataCacheProcessor(cacheManager);
    // 会自动注册到cacheManager
    new LocalMemoryCacheProcessor(cacheManager);
```

### 2、 初始化SqlConnectionPoolManager（连接池管理）

这边需要注意：框架默认只提供JDBC直连的方式，因为不明确其他项目所使用的连接池方案，所以只提供了扩展接入方式，具体参考 [自定义SqlConnectionPool]()

```
    SqlConnectionPoolManager sqlConnectionPoolManager = new SqlConnectionPoolManager();
    //自动注册到sql连接管理
    new DefaultJdbcDirectConnectPool(sqlConnectionPoolManager);
```

### 3、 初始化DataQueryProcessorManager（查询器）

```
    //初始化DataQueryProcessorManager
    DataQueryProcessorManager dataQueryProcessorManager = new DataQueryProcessorManager();
    //自动注册到DataQueryProcessorManager
    new SqlDataQueryProcessor(sqlConnectionPoolManager, dataQueryProcessorManager);
```

### 4、初始化SqlEngine（渲染sql的服务）

关于SqlEngine是一个独立可用的模板引擎，更多详细用法，参考 [FlashSqlEngine](https://github.com/flash-query/FlashSqlEngine)

```
        // 初始化SqlEngine
        FlashSqlEngine sqlEngine = new FlashSqlEngine();
        // 通过配置文件加载的方式载入所有sqlId 对应的模板内容
        sqlEngine.loadConfig("test-sql-id-mapper.xml");
        // 默认提供了Mybatis的模板方式，也可以扩展别的
        sqlEngine.registerSqlParseProvider(MYBATIS_SQL_TYPE, new DefaultMybatisSqlParseProvider());

```

### 5、初始化字段转换器

当前1.0版本只支持字段脱敏的转换

```
    
     //初始化系统的字段转换器
     List<FieldValueConvertor> desensitizeFieldValueConvertors = Lists.newArrayList(new DesensitizeFieldValueConvertor());
        
```

### 6、构造场景查询

```
    
    dataQueryScenarioProcessor = new DataQueryScenarioProcessorImpl(cacheManager, dataQueryProcessorManager, sqlEngine, desensitizeFieldValueConvertors);
    
```

### 7、开始查询

```
    //构造一个场景配置
    DataQueryScenario scenario = buildDefaultScenario();
    // 入参
    JSONObject params = JSONUtil.createObj();
    params.set("id", 1L);
    // 查询得到的结果
    DataQueryResponse<JSON> response = dataQueryScenarioProcessor.process(scenario, params);
    logger.info("{}", response);

```

### 完整代码

完整代码参考 [DataQueryScenarioProcessImplTest.java](https://github.com/flash-query/flash-query-framework/blob/main/flash-query-framework/src/test/java/io/github/xingchuan/query/provider/processor/senario/DataQueryScenarioProcessorImplTest.java)

### 场景配置字段解释

```
    

```


