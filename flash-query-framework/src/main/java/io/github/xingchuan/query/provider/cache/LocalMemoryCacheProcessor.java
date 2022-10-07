package io.github.xingchuan.query.provider.cache;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.json.JSON;
import io.github.xingchuan.query.api.processor.cache.DataCacheProcessor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 本地应用缓存
 *
 * @author xingchuan.qxc
 * @since 1.0
 */
public class LocalMemoryCacheProcessor implements DataCacheProcessor {

    private Logger logger = LoggerFactory.getLogger(LocalMemoryCacheProcessor.class);

    /**
     * LRU策略，最多1000个元素，默认一小时失效
     */
    private Cache<String, JSON> localCache = CacheUtil.newLRUCache(1000, 3600000);

    @Override
    public void saveCache(String key, JSON record) {
        if (StringUtils.isBlank(key)) {
            logger.error("cache key can't be empty.");
            throw new IllegalArgumentException();
        }
        localCache.put(key, record);
    }

    @Override
    public JSON readCache(String key) {
        if (StringUtils.isBlank(key)) {
            logger.error("cache key can't be empty.");
            throw new IllegalArgumentException();
        }
        return localCache.get(key);
    }
}
