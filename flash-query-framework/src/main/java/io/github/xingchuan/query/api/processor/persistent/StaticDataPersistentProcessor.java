package io.github.xingchuan.query.api.processor.persistent;

/**
 * 静态数据保存处理
 *
 * @author xingchuan.qxc
 * @date 2022/6/4
 */
public interface StaticDataPersistentProcessor<T> {


    /**
     * 持久化静态化结果数据到指定的存储位置
     *
     * @param dataJson 待保存的数据
     * @param key      保存到存储的key
     * @return 存储之后返回的结果（OPTIONAL）
     */
    String storeStaticData(T dataJson, String key);


    /**
     * 从存储中得到指定的静态化结果数据
     *
     * @param key 存储key
     * @return 对应的静态化结果数据
     */
    T getStaticData(String key);
}
