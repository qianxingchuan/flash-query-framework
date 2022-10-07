package io.github.xingchuan.query.provider.cache;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import io.github.xingchuan.query.api.domain.error.CommonError;
import io.github.xingchuan.query.api.processor.cache.DataCacheProcessor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;

/**
 * 文件缓存
 *
 * @author xingchuan.qxc
 * @since 1.0
 */
public class FileDataCacheProcessor implements DataCacheProcessor {

    private Logger logger = LoggerFactory.getLogger(FileDataCacheProcessor.class);

    @Override
    public void saveCache(String key, JSON record) {
        if (StringUtils.isBlank(key)) {
            throw CommonError.NOT_SUPPORT_OPERATION().parseErrorMsg(Dict.create().set("operation", "fileKey is empty")).newException();
        }
        ByteArrayInputStream dataStream = IoUtil.toUtf8Stream(record.toString());
        byte[] dataBytes = IoUtil.readBytes(dataStream);
        FileUtil.writeBytes(dataBytes, key);
        logger.info("key {} persisted.", key);
        String version = DigestUtil.md5Hex(dataBytes).toUpperCase();
        logger.info("cache persisted {} key,version is {} ", key, version);
    }

    @Override
    public JSON readCache(String key) {
        if (StringUtils.isBlank(key)) {
            throw CommonError.NOT_SUPPORT_OPERATION().parseErrorMsg(Dict.create().set("operation", "fileKey is empty")).newException();
        }
        String content = IoUtil.readUtf8(FileUtil.getInputStream(key));
        return JSONUtil.parse(content);
    }
}
