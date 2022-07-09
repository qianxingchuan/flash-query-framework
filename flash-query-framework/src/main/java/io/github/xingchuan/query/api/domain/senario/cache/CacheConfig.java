package io.github.xingchuan.query.api.domain.senario.cache;

import cn.hutool.json.JSONObject;
import io.github.xingchuan.query.api.domain.enums.CacheType;

import java.io.Serializable;

/**
 * 对于输出查询场景的缓存处理
 *
 * @author xingchuan.qxc
 * @date 2022/7/9
 */
public class CacheConfig implements Serializable {

    /**
     * 缓存code
     */
    private String code;

    /**
     * 缓存类型
     */
    private CacheType cacheType;

    /**
     * 缓存的其他配置
     */
    private JSONObject properties;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CacheType getCacheType() {
        return cacheType;
    }

    public void setCacheType(CacheType cacheType) {
        this.cacheType = cacheType;
    }

    public JSONObject getProperties() {
        return properties;
    }

    public void setProperties(JSONObject properties) {
        this.properties = properties;
    }
}