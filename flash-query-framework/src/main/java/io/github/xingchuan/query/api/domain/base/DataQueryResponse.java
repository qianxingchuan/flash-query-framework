package io.github.xingchuan.query.api.domain.base;

import java.io.Serializable;

/**
 * 数据查询返回
 *
 * @author xingchuan.qxc
 * @since 1.0
 */
public class DataQueryResponse<T> implements Serializable {

    /**
     * 查询成功 true? false?
     */
    private boolean success;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 关联的数据
     */
    private T data;

    public static <T> DataQueryResponse<T> ofSuccess(T data) {
        DataQueryResponse<T> response = new DataQueryResponse<>();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }

    public static <T> DataQueryResponse<T> ofFailure(String errorCode, String errorMsg, T data) {
        DataQueryResponse<T> response = new DataQueryResponse<>();
        response.setSuccess(false);
        response.setErrorCode(errorCode);
        response.setErrorMsg(errorMsg);
        response.setData(data);
        return response;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }


    @Override
    public String toString() {
        return "DataQueryResponse{" +
                "success=" + success +
                ", errorCode='" + errorCode + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
