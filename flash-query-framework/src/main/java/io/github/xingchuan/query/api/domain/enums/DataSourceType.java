package io.github.xingchuan.query.api.domain.enums;

/**
 * 数据源类型
 *
 * @author xingchuan.qxc
 * @date 2022/6/4
 */
public enum DataSourceType {

    /**
     * 数据库类型
     */
    DB,

    /**
     * API类型
     */
    API,

    /**
     * 其他框架没有内置的类型，支持用户自定义
     */
    CUSTOMIZE

}
