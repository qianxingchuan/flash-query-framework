package io.github.xingchuan.query.provider.helper;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * 构建响应体
 *
 * @author xingchuan.qxc
 * @since 1.0
 */
public class ResponseBuilder {

    /**
     * 构建响应
     *
     * @param success   成功标识
     * @param errorCode 错误码
     * @param errorMsg  错误信息
     * @param <T>       泛型对象
     * @param data      待返回的数据
     * @return 结果JSON
     */
    public static <T> JSONObject buildResponse(boolean success, String errorCode, String errorMsg, T data) {
        JSONObject ret = JSONUtil.createObj();
        ret.set("success", success);
        ret.set("errorCode", errorCode);
        ret.set("errorMsg", errorMsg);
        ret.set("data", data);
        return ret;
    }

    public static JSONObject buildResponse(boolean success, String errorCode, String errorMsg) {
        return buildResponse(success, errorCode, errorMsg, null);
    }
}
