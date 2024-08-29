package com.dwei.common.utils;

import cn.hutool.core.io.FileUtil;

import java.io.File;

/**
 * 临时文件工具类
 *
 * @author hww
 */
public class TmpFileUtils {

    private static final String DEFAULT_PREFIX = "dwei_tmp#";
    private static final String DEFAULT_SUFFIX = ".tmp";

    public static File createTmpFile() {
        return FileUtil.createTempFile(DEFAULT_PREFIX, DEFAULT_SUFFIX, null, true);
    }

    public static File createTmpFile(File dir) {
        return FileUtil.createTempFile(DEFAULT_PREFIX, DEFAULT_SUFFIX, dir, true);
    }

    public static File createTmpFile(String suffix) {
        return FileUtil.createTempFile(DEFAULT_PREFIX, suffix, null, true);
    }

    public static File createTmpFile(String suffix, File dir) {
        return FileUtil.createTempFile(DEFAULT_PREFIX, suffix, dir, true);
    }

    public static File createTmpFile(String prefix, String suffix, File dir) {
        return FileUtil.createTempFile(prefix, suffix, dir, true);
    }

}
