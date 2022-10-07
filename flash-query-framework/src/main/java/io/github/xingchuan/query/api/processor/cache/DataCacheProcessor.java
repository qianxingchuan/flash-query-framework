package io.github.xingchuan.query.api.processor.cache;

import cn.hutool.json.JSON;

/**
 * 数据缓存的定义
 *
 * @author xingchuan.qxc
 * @since 1.0
 */
public interface DataCacheProcessor {

    /**
     * 保存到缓存
     *
     * @param key    保存的文件key
     * @param record 保存的内容
     */
    void saveCache(String key, JSON record);

    /**
     * 从缓存读取一个内容
     *
     * @param key 文件key
     * @return 缓存的内容
     */
    JSON readCache(String key);
}
