package io.github.xingchuan.query.provider.processor.persistent;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import io.github.xingchuan.query.api.domain.error.CommonError;
import io.github.xingchuan.query.api.processor.persistent.StaticDataPersistentProcessor;
import org.apache.commons.lang3.StringUtils;

/**
 * @author xingchuan.qxc
 * @date 2022/6/4
 */
public class LocalStorageStaticDataPersistentProcessor implements StaticDataPersistentProcessor<JSON> {

    public static final String LOCAL_STORAGE_TYPE = "LOCAL_STORAGE_STATIC_DATA";

    @Override
    public String storeStaticData(JSON dataJson, String key) {
        if (StringUtils.isBlank(key)) {
            throw CommonError.NOT_SUPPORT_OPERATION().parseErrorMsg(Dict.create().set("operation", "fileKey is empty")).newException();
        }

        IoUtil.writeUtf8(FileUtil.getOutputStream(key), true, dataJson);
        return null;
    }

    @Override
    public JSON getStaticData(String key) {
        if (StringUtils.isBlank(key)) {
            throw CommonError.NOT_SUPPORT_OPERATION().parseErrorMsg(Dict.create().set("operation", "fileKey is empty")).newException();
        }
        String content = IoUtil.readUtf8(FileUtil.getInputStream(key));
        return JSONUtil.parse(content);
    }

    @Override
    public String storeType() {
        return LOCAL_STORAGE_TYPE;
    }
}
