package io.github.xingchuan.query.api.domain.enums;

/**
 * 脱敏涉及的类型
 *
 * @author xingchuan.qxc
 * @date 2022/6/4
 */
public enum DesensitizedType {
    /**
     * 用户ID
     */
    USER_ID,
    /**
     * 中文名
     */
    CHINESE_NAME,
    /**
     * 身份证
     */
    ID_CARD,
    /**
     * 座机号码
     */
    FIXED_PHONE,
    /**
     * 手机号码
     */
    MOBILE_PHONE,
    /**
     * 地址
     */
    ADDRESS,
    /**
     * 邮件
     */
    EMAIL,
    /**
     * 密码
     */
    PASSWORD,
    /**
     * 车牌号
     */
    CAR_LICENSE,
    /**
     * 银行卡
     */
    BANK_CARD
}