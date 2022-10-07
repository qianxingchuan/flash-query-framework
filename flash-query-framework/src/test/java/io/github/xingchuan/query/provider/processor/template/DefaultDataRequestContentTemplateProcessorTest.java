package io.github.xingchuan.query.provider.processor.template;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import io.github.xingchuan.sql.engine.FlashSqlEngine;
import io.github.xingchuan.sql.provider.impl.DefaultMybatisSqlParseProvider;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.github.xingchuan.sql.provider.impl.DefaultMybatisSqlParseProvider.MYBATIS_SQL_TYPE;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * @author xingchuan.qxc
 * @date 2022/7/9
 */
public class DefaultDataRequestContentTemplateProcessorTest {

    private Logger logger = LoggerFactory.getLogger(DefaultDataRequestContentTemplateProcessorTest.class);

    @Test
    public void test_测试默认模板转换器() {
        JSONObject params = JSONUtil.createObj();
        params.set("userCode", "xingchuan.qxc");
        params.set("userCodes", Lists.newArrayList("Jevin", "Vicky", "Erin", "Bobby"));
        // 基础用法
        String templateContent1 = "select * from t_user where user_code = '${userCode}'";
        DefaultDataRequestContentTemplateProcessor templateProcessor = new DefaultDataRequestContentTemplateProcessor();
        String sql = templateProcessor.parseRequestContent(templateContent1, params);
        logger.info("{}", sql);
        assertThat(sql).isEqualTo("select * from t_user where user_code = 'xingchuan.qxc'");

        // sql 注入
        String templateContent12 = "select * from t_user where user_code = '${userCode}'";
        JSONObject params12 = JSONUtil.createObj();
        params12.set("userCode", "' or 1 = 1 or a = '");
        String sql12 = templateProcessor.parseRequestContent(templateContent12, params12);
        logger.info("{}", sql12);

        // 循环
        String templateContent2 = "select * from t_user where user_code in (<%for(user in userCodes) {%>" +
                "<% if (!userLP.last) {%>" +
                "'${user}'," +
                "<% } else { %>" +
                "'${user}'" +
                "<% } %>" +
                "<% } %>)";
        String sql2 = templateProcessor.parseRequestContent(templateContent2, params);
        logger.info("{}", sql2);
        assertThat(sql2).isEqualTo("select * from t_user where user_code in ('Jevin','Vicky','Erin','Bobby')");
    }

    @Test
    public void test_测试mybatis模板转换器() {
        JSONObject params = JSONUtil.createObj();
        params.set("userCode", "xingchuan.qxc");
        params.set("userCodes", Lists.newArrayList("Jevin", "Vicky", "Erin", "Bobby"));
        // 基础用法
        String templateContent1 = "select * from t_user where user_code = #{userCode}";

        FlashSqlEngine sqlEngine = new FlashSqlEngine();
        sqlEngine.registerSqlParseProvider(MYBATIS_SQL_TYPE, new DefaultMybatisSqlParseProvider());
        MyBatisTemplateProcessor templateProcessor = new MyBatisTemplateProcessor(sqlEngine);
        String sql = templateProcessor.parseRequestContent(templateContent1, params);
        logger.info("{}", sql);
        assertThat(sql).isEqualTo("select * from t_user where user_code = 'xingchuan.qxc' ");

        // sql 注入
        String templateContent12 = "select * from t_user where user_code = '${userCode}'";
        JSONObject params12 = JSONUtil.createObj();
        params12.set("userCode", "' or 1 = 1 or a = '");
        String sql12 = templateProcessor.parseRequestContent(templateContent12, params12);
        logger.info("{}", sql12);

        // 循环
        String templateContent2 = "select * from t_user where user_code in (" +
                "<foreach collection = \"userCodes\" item = \"code\" separator = \",\">" +
                "#{code}" +
                "</foreach>" +
                ")";
        String sql2 = templateProcessor.parseRequestContent(templateContent2, params);
        logger.info("{}", sql2);
        assertThat(sql2).isEqualTo("select * from t_user where user_code in (  'Jevin' , 'Vicky' , 'Erin' , 'Bobby' ) ");
    }
}