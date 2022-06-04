package io.github.xingchuan.query.api.domain.error;

/**
 * 框架的运行时异常
 *
 * @author xingchuan.qxc
 * @date 2022/6/4
 */
public class JevinRuntimeException extends RuntimeException {

    public JevinRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
