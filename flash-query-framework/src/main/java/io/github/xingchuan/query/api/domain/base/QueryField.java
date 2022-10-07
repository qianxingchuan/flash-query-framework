package io.github.xingchuan.query.api.domain.base;

import io.github.xingchuan.query.api.domain.enums.FieldDataType;

import java.io.Serializable;
import java.util.Set;

/**
 * 涉及到的查询、返回字段的配置
 *
 * @author xingchuan.qxc
 * @since  1.0
 */
public class QueryField implements Serializable {

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 别名
     */
    private String aliasName;

    /**
     * 字段类型
     */
    private FieldDataType fieldDataType;

    /**
     * 值转换器，一般用作脱敏，或者转换值得特别处理器
     */
    private Set<String> fieldConvertTypes;


    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public FieldDataType getFieldDataType() {
        return fieldDataType;
    }

    public void setFieldDataType(FieldDataType fieldDataType) {
        this.fieldDataType = fieldDataType;
    }

    public Set<String> getFieldConvertTypes() {
        return fieldConvertTypes;
    }

    public void setFieldConvertTypes(Set<String> fieldConvertTypes) {
        this.fieldConvertTypes = fieldConvertTypes;
    }
}
