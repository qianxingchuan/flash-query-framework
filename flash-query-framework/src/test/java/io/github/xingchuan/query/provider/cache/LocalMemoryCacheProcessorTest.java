package io.github.xingchuan.query.provider.cache;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xingchuan.qxc
 * @since
 */
public class LocalMemoryCacheProcessorTest {

    @Test
    public void test_saveLocalVmCache_readLocalVmCache_ok() {
        String cacheKey = System.currentTimeMillis() + ".json";
        JSONObject obj = JSONUtil.createObj();
        obj.set("code", "xingchuan.qxc");
        obj.set("name", "钱幸川");

        LocalMemoryCacheProcessor localMemoryCacheProcessor = new LocalMemoryCacheProcessor(new CacheManager());
        localMemoryCacheProcessor.saveCache(cacheKey, obj);

        JSON jsonInCache = localMemoryCacheProcessor.readCache(cacheKey);
        JSONObject target = (JSONObject) jsonInCache;
        assertThat(target.getStr("code")).isEqualTo("xingchuan.qxc");
        assertThat(target.getStr("name")).isEqualTo("钱幸川");

        JSON json = localMemoryCacheProcessor.readCache("not-exist-key");
        assertThat(json).isNull();
    }
}