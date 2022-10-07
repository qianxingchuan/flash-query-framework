package io.github.xingchuan.query.provider.cache;

import cn.hutool.core.map.MapUtil;
import io.github.xingchuan.query.api.processor.cache.DataCacheProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 缓存管理器
 *
 * @author xingchuan.qxc
 * @since 1.0
 */
public class CacheManager {

    private final Map<String, DataCacheProcessor> cacheProcessorMap = MapUtil.newHashMap();

    private final Logger logger = LoggerFactory.getLogger(CacheManager.class);

    /**
     * 注册一个缓存
     *
     * @param type           缓存类型
     * @param cacheProcessor 缓存处理器
     */
    public void registerCacheProcessor(DataCacheProcessor cacheProcessor) {
        String type = cacheProcessor.cacheType();
        cacheProcessorMap.put(type, cacheProcessor);
        logger.info("cache {} registered.", type);
    }

    /**
     * 获得对应的缓存处理器
     *
     * @param type 缓存类型
     * @return 对应的缓存处理器
     */
    public DataCacheProcessor getCacheProcessor(String type) {
        return cacheProcessorMap.get(type);
    }
}
