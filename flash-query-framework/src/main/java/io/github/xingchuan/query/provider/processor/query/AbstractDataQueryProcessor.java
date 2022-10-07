package io.github.xingchuan.query.provider.processor.query;

import io.github.xingchuan.query.api.processor.query.DataQueryProcessor;

/**
 * 抽象的数据查询器
 *
 * @author xingchuan.qxc
 * @since 1.0
 */
public abstract class AbstractDataQueryProcessor implements DataQueryProcessor {

    private final DataQueryProcessorManager dataQueryProcessorManager;

    public AbstractDataQueryProcessor(DataQueryProcessorManager dataQueryProcessorManager) {
        this.dataQueryProcessorManager = dataQueryProcessorManager;
        this.dataQueryProcessorManager.registerDataQueryProcessor(this);
    }

}
