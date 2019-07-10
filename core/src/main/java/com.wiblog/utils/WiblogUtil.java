package com.wiblog.utils;

import com.wiblog.entity.User;

import org.apache.commons.lang3.StringUtils;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import javax.servlet.http.HttpServletRequest;

/**
 * TODO 描述
 *
 * @author pwm
 * @date 2019/7/1
 */
public class WiblogUtil {

    /**
     * markdown解析器
     */
    private static Parser parser = Parser.builder().build();

    /**
     * markdown转换为html
     *
     * @param markdown
     * @return
     */
    public static String mdToHtml(String markdown) {
        if (StringUtils.isBlank(markdown)) {
            return "";
        }
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String content = renderer.render(document);
        content = Commons.emoji(content);
        return content;
    }

    public static User getLoginUser(HttpServletRequest request){
        return null;
    }
}
