package io.github.xingchuan.query.provider.cache;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.junit.AfterClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xingchuan.qxc
 * @since
 */
public class FileDataCacheProcessorTest {

    private static String LOCAL_TEMP_PATH = "~/test-case/flash-query/";

    @AfterClass
    public static void doAfter() {
        FileUtil.del(LOCAL_TEMP_PATH);
    }

    @Test
    public void test_save_local_file_and_read_ok() {
        String fileKey = LOCAL_TEMP_PATH + System.currentTimeMillis() + ".json";
        JSONObject obj = JSONUtil.createObj();
        obj.set("code", "xingchuan.qxc");
        obj.set("name", "钱幸川");

        FileDataCacheProcessor fileDataCacheProcessor = new FileDataCacheProcessor();
        fileDataCacheProcessor.saveCache(fileKey, obj);

        JSON persistValue = fileDataCacheProcessor.readCache(fileKey);
        assertThat(persistValue).isNotNull();
        JSONObject target = (JSONObject) persistValue;
        assertThat(target.getStr("code")).isEqualTo("xingchuan.qxc");
        assertThat(target.getStr("name")).isEqualTo("钱幸川");
    }

}