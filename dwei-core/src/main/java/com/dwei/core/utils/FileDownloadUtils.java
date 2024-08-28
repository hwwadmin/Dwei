package com.dwei.core.utils;

import com.dwei.common.exception.UtilsException;
import com.dwei.common.utils.Assert;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * 文件下载工具类
 *
 * @author hww
 */
@Slf4j
public abstract class FileDownloadUtils {

    public static void download(String filePath, HttpServletResponse response) {
        download(new File(filePath).getName(), filePath, response);
    }

    public static void download(String fileName, InputStream inputStream, HttpServletResponse response) {
        download(fileName, inputStream, null, response);
    }

    public static void download(String fileName, String filePath, HttpServletResponse response) {
        File file = new File(filePath);
        Assert.isTrue(file.exists() && file.isFile(), "文件不存在或路径指向为文件夹:{}", filePath);
        try {
            download(fileName, new FileInputStream(filePath), file.length(), response);
        } catch (FileNotFoundException e) {
            throw UtilsException.exception("文件下载失败", e);
        }
    }

    public static void download(String fileName, InputStream inputStream, Long length, HttpServletResponse response) {
        try (
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
        ) {
            response.setContentType("application/octet-stream");
//            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            if (Objects.nonNull(length)) response.setHeader("Content-Length", String.valueOf(length));
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) bos.write(buff, 0, bytesRead);
            bos.flush();
        } catch (Exception e) {
            throw UtilsException.exception("文件下载失败", e);
        }
    }

}
