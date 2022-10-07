package io.github.xingchuan.query.provider.processor.template;

import cn.hutool.json.JSONObject;
import io.github.xingchuan.query.api.processor.template.DataRequestContentTemplateProcessor;
import io.github.xingchuan.sql.engine.FlashSqlEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.xingchuan.sql.provider.impl.DefaultMybatisSqlParseProvider.MYBATIS_SQL_TYPE;

/**
 * mybatis的模板处理
 *
 * @author xingchuan.qxc
 * @date 2022/9/30
 */
public class MyBatisTemplateProcessor implements DataRequestContentTemplateProcessor {

    private Logger logger = LoggerFactory.getLogger(MyBatisTemplateProcessor.class);

    private FlashSqlEngine sqlEngine;

    public MyBatisTemplateProcessor(FlashSqlEngine sqlEngine) {
        this.sqlEngine = sqlEngine;
    }

    @Override
    public String parseRequestContent(String template, JSONObject params) {
        return sqlEngine.parseSql(template, params, MYBATIS_SQL_TYPE);
    }

    @Override
    public String processorType() {
        return MYBATIS_TEMPLATE_PROCESSOR;
    }


}
