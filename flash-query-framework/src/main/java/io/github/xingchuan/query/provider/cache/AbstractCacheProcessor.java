package io.github.xingchuan.query.provider.cache;

import io.github.xingchuan.query.api.processor.cache.DataCacheProcessor;

/**
 * 缓存处理器的抽象类
 *
 * @author xingchuan.qxc
 * @since 1.0
 */
public abstract class AbstractCacheProcessor implements DataCacheProcessor {

    /**
     * 缓存管理
     */
    private CacheManager cacheManager;

    public AbstractCacheProcessor(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
        //注册到缓存管理
        this.cacheManager.registerCacheProcessor(this);
    }


}
