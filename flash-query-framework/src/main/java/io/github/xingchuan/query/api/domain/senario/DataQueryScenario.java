package io.github.xingchuan.query.api.domain.senario;

import io.github.xingchuan.query.api.domain.base.DataSource;
import io.github.xingchuan.query.api.domain.base.QueryField;
import io.github.xingchuan.query.api.domain.senario.cache.CacheConfig;

import java.io.Serializable;
import java.util.List;

/**
 * 针对前端查询的一组配置
 * 包括查询模板， 数据源
 *
 * @author xingchuan.qxc
 * @since  1.0
 */
public class DataQueryScenario implements Serializable {

    /**
     * 查询指定的code
     */
    private String code;

    /**
     * 数据查询场景名称
     */
    private String name;

    /**
     * 查询模板
     */
    private String queryTemplateContent;

    /**
     * 针对入参的特殊处理,如果不配置，只使用默认的前端输入作为参数，渲染模板
     */
    private List<QueryField> inputFields;

    /**
     * 针对出参的特殊处理 ,如果不配置，原样输出
     */
    private List<QueryField> outputFields;

    /**
     * 对应查询的数据源
     */
    private DataSource dataSource;

    /**
     * 缓存处理
     */
    private CacheConfig cacheConfig;

    public CacheConfig getCacheConfig() {
        return cacheConfig;
    }

    public void setCacheConfig(CacheConfig cacheConfig) {
        this.cacheConfig = cacheConfig;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQueryTemplateContent() {
        return queryTemplateContent;
    }

    public void setQueryTemplateContent(String queryTemplateContent) {
        this.queryTemplateContent = queryTemplateContent;
    }

    public List<QueryField> getInputFields() {
        return inputFields;
    }

    public void setInputFields(List<QueryField> inputFields) {
        this.inputFields = inputFields;
    }

    public List<QueryField> getOutputFields() {
        return outputFields;
    }

    public void setOutputFields(List<QueryField> outputFields) {
        this.outputFields = outputFields;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
