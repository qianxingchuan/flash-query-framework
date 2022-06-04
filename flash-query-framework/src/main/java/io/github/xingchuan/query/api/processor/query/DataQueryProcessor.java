package io.github.xingchuan.query.api.processor.query;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import io.github.xingchuan.query.api.domain.base.DataSource;

/**
 * 数据查询的指定processor
 *
 * @author xingchuan.qxc
 * @date 2022/6/4
 */
public interface DataQueryProcessor {

    /**
     * 在数据源dataSource中执行query，得到一个结果
     *
     * @param query      查询涉及的关键，比如sql，api的请求body等
     * @param dataSource 查询数据源，比如mysql，另一个api服务等
     * @return 结果集
     */
    JSONArray queryData(String query, DataSource dataSource);

    /**
     * 在数据源dataSource中执行一个更新操作
     *
     * @param update     更新请求，比如一个更新的SQL，api的请求body等
     * @param dataSource 数据源，比如mysql，另一个api服务
     * @return 执行数据源的返回结果，转换成统一的json格式返回
     */
    JSON executeUpdate(String update, DataSource dataSource);

    /**
     * 处理器的类型，全局应该唯一
     *
     * @return 处理器的名称
     */
    String processorType();
}