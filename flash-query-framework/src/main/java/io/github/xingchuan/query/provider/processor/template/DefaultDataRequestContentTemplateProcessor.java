package io.github.xingchuan.query.provider.processor.template;

import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.json.JSONObject;
import io.github.xingchuan.query.api.processor.template.DataRequestContentTemplateProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认的查询模板更新
 *
 * 注意： 有sql注入风险
 *
 * @author xingchuan.qxc
 * @since 1.0
 */
public class DefaultDataRequestContentTemplateProcessor implements DataRequestContentTemplateProcessor {


    private Logger logger = LoggerFactory.getLogger(DefaultDataRequestContentTemplateProcessor.class);


    @Override
    public String parseRequestContent(String templateContent, JSONObject params) {
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig());
        Template template = engine.getTemplate(templateContent);
        return template.render(params);
    }

    @Override
    public String processorType() {
        return DEFAULT_TEMPLATE_PARSER;
    }
}
