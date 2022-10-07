package io.github.xingchuan.query.provider.processor.senario;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import io.github.xingchuan.query.api.domain.base.DataQueryResponse;
import io.github.xingchuan.query.api.domain.base.DataSource;
import io.github.xingchuan.query.api.domain.base.QueryField;
import io.github.xingchuan.query.api.domain.senario.DataQueryScenario;
import io.github.xingchuan.query.api.domain.senario.cache.CacheConfig;
import io.github.xingchuan.query.api.processor.cache.DataCacheProcessor;
import io.github.xingchuan.query.api.processor.convertor.FieldValueConvertor;
import io.github.xingchuan.query.api.processor.query.DataQueryProcessor;
import io.github.xingchuan.query.api.processor.senario.DataQueryScenarioProcessor;
import io.github.xingchuan.query.provider.cache.CacheManager;
import io.github.xingchuan.query.provider.processor.query.DataQueryProcessorManager;
import io.github.xingchuan.sql.engine.FlashSqlEngine;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据查询场景的处理
 *
 * @author xingchuan.qxc
 * @since 1.0
 */
public class DataQueryScenarioProcessorImpl implements DataQueryScenarioProcessor {

    private Logger logger = LoggerFactory.getLogger(DataQueryScenarioProcessor.class);

    /**
     * 缓存管理
     */
    private CacheManager cacheManager;

    /**
     * 资源查询器管理
     */
    private DataQueryProcessorManager dataQueryProcessorManager;

    /**
     * sqlEngine
     */
    private FlashSqlEngine sqlEngine;

    /**
     * 字段转换器
     */
    private List<FieldValueConvertor> fieldValueConvertors;

    public DataQueryScenarioProcessorImpl(CacheManager cacheManager, DataQueryProcessorManager dataQueryProcessorManager, FlashSqlEngine sqlEngine, List<FieldValueConvertor> fieldValueConvertors) {
        this.cacheManager = cacheManager;
        this.dataQueryProcessorManager = dataQueryProcessorManager;
        this.sqlEngine = sqlEngine;
        this.fieldValueConvertors = fieldValueConvertors;
    }

    @Override
    public DataQueryResponse<JSON> process(DataQueryScenario scenario, JSONObject params) throws Exception {
        if (scenario == null) {
            logger.error("scenario is null");
            throw new IllegalArgumentException();
        }

        String scenarioCode = scenario.getCode();
        CacheConfig cacheConfig = scenario.getCacheConfig();

        DataCacheProcessor cacheProcessor = fetchDataCacheProcessor(cacheConfig);
        JSON cacheValue = readCacheValue(cacheProcessor, scenarioCode, params);
        if (cacheValue != null) {
            //缓存有值，直接返回
            return DataQueryResponse.ofSuccess(cacheValue);
        }

        DataSource dataSource = scenario.getDataSource();
        // 输入字段(1.0版本暂不处理）
        List<QueryField> inputFields = scenario.getInputFields();
        String targetSql = parseTargetSql(scenario, params);

        DataQueryProcessor dataQueryProcessor = dataQueryProcessorManager.getDataQueryProcessor(dataSource.getDataSourceType().name());
        // 查询得到结果
        JSONArray array = dataQueryProcessor.queryData(targetSql, dataSource);
        processResultArray(scenario, array);
        // 保存缓存
        saveCacheValue(cacheProcessor, scenarioCode, params, array);
        return DataQueryResponse.ofSuccess(array);
    }

    /**
     * 保存缓存值
     *
     * @param cacheProcessor 缓存处理器
     * @param scenarioCode   场景code
     * @param params         系统入参
     * @param array          查询结果
     */
    private void saveCacheValue(DataCacheProcessor cacheProcessor, String scenarioCode, JSONObject params, JSONArray array) {
        if (cacheProcessor == null) {
            return;
        }
        String cacheKey = buildCacheKey(scenarioCode, params);
        cacheProcessor.saveCache(cacheKey, array);
    }

    /**
     * 处理结果返回值
     *
     * @param scenario 场景配置
     * @param array    查询的返回字段
     */
    private void processResultArray(DataQueryScenario scenario, JSONArray array) {
        // 输出字段 （对于出参的处理）
        List<QueryField> outputFields = scenario.getOutputFields();
        if (CollectionUtil.isEmpty(array) || CollectionUtil.isEmpty(outputFields)) {
            return;
        }


        // 标准化
        fieldValueConvertors = fieldValueConvertors == null ? new ArrayList<>() : fieldValueConvertors;
        // key -> fieldName value -> itself
        Map<String, QueryField> outputFieldsMap = outputFields.stream().collect(Collectors.toMap(QueryField::getFieldName, Function.identity(), (o1, o2) -> o2));
        // 转换出参
        for (int i = 0; i < array.size(); i++) {
            JSONObject recordJson = array.getJSONObject(i);
            processResultRecordItem(outputFieldsMap, recordJson);
        }

    }

    /**
     * 处理返回结果集的某条记录的字段
     *
     * @param outputFieldsMap 出参配置
     * @param recordJson      出参记录
     */
    private void processResultRecordItem(Map<String, QueryField> outputFieldsMap, JSONObject recordJson) {
        Set<String> keys = recordJson.keySet();
        for (String key : keys) {
            QueryField queryFieldConfig = outputFieldsMap.get(key);
            if (queryFieldConfig == null) {
                continue;
            }
            Object value = recordJson.get(key);
            String aliasName = queryFieldConfig.getAliasName();
            if (StringUtils.isBlank(aliasName)) {
                aliasName = key;
            }

            Set<String> fieldConvertTypes = queryFieldConfig.getFieldConvertTypes();
            if (CollectionUtil.isEmpty(fieldConvertTypes)) {
                recordJson.set(aliasName, value);
                continue;
            }
            List<FieldValueConvertor> shouldProcessFieldValueConvertor = fieldValueConvertors.stream().filter(convertor -> fieldConvertTypes.contains(convertor.convertTypeCode())).sorted(Comparator.comparingInt(FieldValueConvertor::getIndex))
                    .collect(Collectors.toList());
            for (FieldValueConvertor fieldValueConvertor : shouldProcessFieldValueConvertor) {
                value = fieldValueConvertor.processConvert(value, queryFieldConfig.getConvertParams());
            }
            recordJson.set(aliasName, value);
        }
    }

    /**
     * 查询目标执行的查询sql
     *
     * @param scenario 场景配置
     * @param params   入参
     * @return 目标sql
     */
    private String parseTargetSql(DataQueryScenario scenario, JSONObject params) {
        // sqlId
        String sqlId = scenario.getSqlId();
        // sql template
        String queryTemplateContent = scenario.getQueryTemplateContent();

        String templateProviderType = scenario.getTemplateProviderType();
        // sql template 优先级比sqlId更高
        String targetSql = null;
        if (StringUtils.isNotBlank(queryTemplateContent)) {
            targetSql = sqlEngine.parseSql(queryTemplateContent, params, templateProviderType);
        } else {
            targetSql = sqlEngine.parseSqlWithSqlId(sqlId, params, templateProviderType);
        }
        return targetSql;
    }

    /**
     * 缓存的处理
     *
     * @param cacheProcessor 缓存处理器
     * @param scenarioCode   场景code
     * @param params         前端输入的参数
     * @return 缓存中的值
     */
    private JSON readCacheValue(DataCacheProcessor cacheProcessor, String scenarioCode, JSONObject params) {
        if (cacheProcessor == null) {
            return null;
        }
        String cacheKey = buildCacheKey(scenarioCode, params);
        return cacheProcessor.readCache(cacheKey);
    }

    /**
     * 构建缓存cacheKey
     *
     * @param scenarioCode 场景code
     * @param params       前端输入的参数
     * @return 缓存key
     */
    private String buildCacheKey(String scenarioCode, JSONObject params) {
        TreeMap<String, Object> inputParamsSorted = MapUtil.sort(params);
        return scenarioCode + DigestUtil.md5Hex(ObjectUtil.serialize(inputParamsSorted));
    }

    /**
     * 获得对应的缓存处理器
     *
     * @param cacheConfig 缓存配置
     * @return
     */
    private DataCacheProcessor fetchDataCacheProcessor(CacheConfig cacheConfig) {
        if (cacheConfig == null || StringUtils.isBlank(cacheConfig.getCacheType())) {
            return null;
        }
        String cacheType = cacheConfig.getCacheType();
        return cacheManager.getCacheProcessor(cacheType);
    }
}
