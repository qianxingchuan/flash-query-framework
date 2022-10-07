package io.github.xingchuan.query.provider.processor.convertor;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.json.JSONObject;
import io.github.xingchuan.query.api.processor.convertor.FieldValueConvertor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 脱敏相关的处理
 *
 * @author xingchuan.qxc
 * @since  1.0
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
        // 取值见 @see cn.hutool.core.util.DesensitizedUtil.DesensitizedType
        String desensitizeType = params != null ? params.getStr("desensitizeType") : StringUtils.EMPTY;

        DesensitizedUtil.DesensitizedType desensitizedType = DesensitizedUtil.DesensitizedType.PASSWORD;
        if (StringUtils.isNotBlank(desensitizeType)) {
            desensitizedType = DesensitizedUtil.DesensitizedType.valueOf(desensitizeType);
        }
        return DesensitizedUtil.desensitized(inputValue, desensitizedType);
    }
}
