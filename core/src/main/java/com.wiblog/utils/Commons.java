package com.wiblog.utils;

import com.vdurmont.emoji.EmojiParser;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * TODO 描述
 *
 * @author pwm
 * @date 2019/7/1
 */
@Component
public final class Commons {

    /**
     * An :grinning:awesome :smiley:string &#128516;with a few :wink:emojis!
     * <p>
     * 这种格式的字符转换为emoji表情
     *
     * @param value
     * @return
     */
    public static String emoji(String value) {
        return EmojiParser.parseToUnicode(value);
    }

    /**
     * 显示文章内容，转换markdown为html
     *
     * @param value
     * @return
     */
    public static String article(String value) {
        if (StringUtils.isNotBlank(value)) {
//            value = value.replace("<!--more-->", "\r\n");
            return WiblogUtil.mdToHtml(value);
        }
        return "";
    }
}
