package io.github.xingchuan.query.provider.processor.query;

import cn.hutool.core.map.MapUtil;
import io.github.xingchuan.query.api.processor.query.DataQueryProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 查询器管理
 *
 * @author xingchuan.qxc
 * @since 1.0
 */
public class DataQueryProcessorManager {

    private final Map<String, DataQueryProcessor> dataQueryProcessorMap = MapUtil.newHashMap();

    private final Logger logger = LoggerFactory.getLogger(DataQueryProcessorManager.class);

    /**
     * 数据查询器注册
     *
     * @param processor 数据查询器
     */
    public void registerDataQueryProcessor(DataQueryProcessor processor) {
        String type = processor.processorType();
        dataQueryProcessorMap.put(type, processor);
        logger.info("dataQueryProcessor {} registered.", type);
    }

    /**
     * 通过类型找到一个数据查询器
     *
     * @param type 类型
     * @return 一个数据查询器
     */
    public DataQueryProcessor getDataQueryProcessor(String type) {
        return dataQueryProcessorMap.get(type);
    }
}
