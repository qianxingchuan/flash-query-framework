package io.github.xingchuan.query.api.domain.error;

/**
 * 框架的运行时异常
 *
 * @author xingchuan.qxc
 * @since  1.0
 */
public class JevinRuntimeException extends RuntimeException {

    public JevinRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
