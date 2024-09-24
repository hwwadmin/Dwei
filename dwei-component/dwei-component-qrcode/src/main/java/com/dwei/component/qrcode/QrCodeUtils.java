package com.dwei.component.qrcode;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.dwei.common.constants.AppConstants;
import com.dwei.common.utils.Assert;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;

/**
 * 二维码工具类
 *
 * @author hww
 */
public abstract class QrCodeUtils {

    /**
     * 内容解析
     *
     * @param qrCodeBase64 二维码图片base64
     */
    public static String decode(String qrCodeBase64) {
        return QrCodeUtil.decode(ImgUtil.toImage(removeImgBase64Prefix(qrCodeBase64)));
    }

    /**
     * 二维码生成器
     */
    public static Builder generate() {
        return new Builder();
    }

    public static class Builder {
        /** 内容 */
        private String ctx;
        /** 生成二维码的图片格式 */
        private String targetType;
        /** 二维码配置 */
        private final QrConfig qrConfig;

        private Builder() {
            this.qrConfig = QrConfig.create();
            // 纠错级别默认使用Q
            this.qrConfig.setErrorCorrection(ErrorCorrectionLevel.Q);
        }

        /**
         * 设置内容
         */
        public Builder ctx(String ctx) {
            this.ctx = ctx;
            return this;
        }

        /**
         * 设置二维码的图片格式
         * 默认使用jpeg
         * eg.jpg、jpeg、png
         */
        public Builder type(String type) {
            this.targetType = type;
            return this;
        }

        /**
         * 设置宽度
         */
        public Builder width(int width) {
            this.qrConfig.setWidth(width);
            return this;
        }

        /**
         * 设置高度
         */
        public Builder height(int height) {
            this.qrConfig.setHeight(height);
            return this;
        }

        /**
         * 设置边距 1~4
         */
        public Builder margin(int margin) {
            this.qrConfig.setMargin(margin);
            return this;
        }

        /**
         * 设置二维码logo
         */
        public Builder logo(String logoBase64) {
            this.qrConfig.setImg(ImgUtil.toImage(removeImgBase64Prefix(logoBase64)));
            return this;
        }

        /**
         * 设置二维码logo
         */
        public Builder logo(File logFile) {
            this.qrConfig.setImg(ImgUtil.read(logFile));
            return this;
        }

        /**
         * 设置二维码中的Logo缩放的比例系数
         * 如5表示长宽最小值的1/5，默认6
         */
        public Builder logoRate(int ratio) {
            this.qrConfig.setRatio(ratio);
            return this;
        }

        /**
         * 设置纠错级别
         */
        public Builder errorCorrectionLevel(ErrorCorrectionLevel level) {
            this.qrConfig.setErrorCorrection(level);
            return this;
        }

        public String toJpg() {
            this.targetType = ImgUtil.IMAGE_TYPE_JPG;
            return create();
        }

        public String toJpeg() {
            this.targetType = ImgUtil.IMAGE_TYPE_JPEG;
            return create();
        }

        public String toPng() {
            this.targetType = ImgUtil.IMAGE_TYPE_PNG;
            return create();
        }

        public String toGif() {
            this.targetType = ImgUtil.IMAGE_TYPE_GIF;
            return create();
        }

        private String create() {
            Assert.isStrNotBlank(this.ctx, "二维码内容为空");
            return QrCodeUtil.generateAsBase64(ctx, qrConfig, targetType);
        }

    }

    /**
     * 取出图片base64的前缀
     * eg. data:image/png;base64,iVBORw.... -> iVBORw....
     */
    private static String removeImgBase64Prefix(String base64) {
        if (!base64.contains(AppConstants.STRING_CSV)) return base64;
        return base64.split(AppConstants.STRING_CSV)[1];
    }

}
