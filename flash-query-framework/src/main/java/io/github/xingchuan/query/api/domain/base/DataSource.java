package io.github.xingchuan.query.api.domain.base;

import cn.hutool.json.JSONObject;
import io.github.xingchuan.query.api.domain.enums.DataSourceType;

import java.io.Serializable;

/**
 * 查询数据源
 *
 * @author xingchuan.qxc
 * @date 2022/6/4
 */
public class DataSource implements Serializable {

    /**
     * 数据源的唯一code
     */
    private String code;

    /**
     * 数据源的类型
     */
    private DataSourceType dataSourceType;

    /**
     * 数据源对应的属性
     */
    private JSONObject properties;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataSourceType getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(DataSourceType dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public JSONObject getProperties() {
        return properties;
    }

    public void setProperties(JSONObject properties) {
        this.properties = properties;
    }
}
