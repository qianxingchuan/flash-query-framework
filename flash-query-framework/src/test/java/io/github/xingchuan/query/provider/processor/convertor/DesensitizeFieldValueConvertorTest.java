package io.github.xingchuan.query.provider.processor.convertor;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.github.xingchuan.query.api.domain.enums.DesensitizedType;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author xingchuan.qxc
 * @since 1.0
 */
public class DesensitizeFieldValueConvertorTest {

    private Logger logger = LoggerFactory.getLogger(DesensitizeFieldValueConvertorTest.class);

    @Test
    public void test_脱敏测试() {
        DesensitizeFieldValueConvertor convertor = new DesensitizeFieldValueConvertor();
        String ret = convertor.processConvert("sample-value");
        logger.info("{} ", ret);
        assertThat(ret).isEqualTo("************");

        JSONObject param = JSONUtil.createObj();
        param.set("desensitizeType", DesensitizedType.FIXED_PHONE);
        String ret2 = convertor.processConvert("13000000019", param);
        logger.info("{}", ret2);
        assertThat(ret2).isEqualTo("1300*****19");


    }
}