package io.github.xingchuan.query.provider.processor.convertor;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.json.JSONObject;
import io.github.xingchuan.query.api.processor.convertor.FieldValueConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xingchuan.qxc
 * @date 2022/6/4
 */
public class DesensitizeFieldValueConvertor implements FieldValueConvertor<String, String> {

    private Logger logger = LoggerFactory.getLogger(DesensitizeFieldValueConvertor.class);

    @Override
    public int getIndex() {
        return 0;
    }

    @Override
    public String processConvert(String inputValue) {

        return processConvert(inputValue, null);
    }

    @Override
    public String processConvert(String inputValue, JSONObject params) {
        // todo 脱敏的相关处理
        return DesensitizedUtil.desensitized(inputValue, DesensitizedUtil.DesensitizedType.PASSWORD);
    }
}
