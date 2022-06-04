package io.github.xingchuan.query.api.processor.convertor;

import cn.hutool.json.JSONObject;

/**
 * 字段值转换器
 *
 * @author xingchuan.qxc
 * @date 2022/6/4
 */
public interface FieldValueConvertor<I, O> {

    /**
     * 一组convert中的执行顺序
     *
     * @return 执行顺序中的index
     */
    int getIndex();

    /**
     * 输入一个值，转换成另一个值返回
     *
     * @param inputValue 输入值
     * @return 输出值
     */
    O processConvert(I inputValue);

    /**
     * 输入一个值，转换成另一个值返回
     *
     * @param inputValue 输入值
     * @param params     涉及到的参数
     * @return 输出值
     */
    O processConvert(I inputValue, JSONObject params);
}
