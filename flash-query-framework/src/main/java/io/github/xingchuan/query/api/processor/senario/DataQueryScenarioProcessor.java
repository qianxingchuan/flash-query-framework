package io.github.xingchuan.query.api.processor.senario;

import cn.hutool.json.JSONObject;
import io.github.xingchuan.query.api.domain.senario.DataQueryScenario;

/**
 * <pre>
 * 基于数据场景的处理
 * 一个数据场景包括 输入参数处理（当前默认按照前端入参） --> 匹配合适的模板 -> 模板引擎渲染查询语句 -> 调用数据层 -> 结果脱敏 -> 返回结果
 * </pre>
 *
 * @author xingchuan.qxc
 * @since 1.0
 */
public interface DataQueryScenarioProcessor {

    /**
     * 处理一个数据场景
     *
     * @param scenario
     * @return
     */
    JSONObject process(DataQueryScenario scenario);

}
