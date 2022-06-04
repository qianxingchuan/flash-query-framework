package io.github.xingchuan.query.api.processor.template;

import cn.hutool.json.JSONObject;

/**
 * 用于生成数据请求涉及的content，比如sql，api的body
 *
 * @author xingchuan.qxc
 * @date 2022/6/4
 */
public interface DataRequestContentTemplateProcessor {

    /**
     * 通过参数和模板，生成对应的content
     *
     * @param template 生成模板
     * @param params   参数列表
     * @return 转换完成之后的content
     */
    String parseRequestContent(String template, JSONObject params);

    /**
     * 对应的处理器类型
     *
     * @return 处理器类型
     */
    String processorType();
}
