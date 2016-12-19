package com.example.framework.http;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhaiJiaYi on 2016/12/19.
 */
public class HtmlParser {
    public HtmlParser() {
    }

    public static List<TagNode> parseNode(String content, String xpath) {
        Object[] result = getByXPath(content, xpath);
        ArrayList tagNodeList = new ArrayList();
        Object[] var4 = result;
        int var5 = result.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Object obj = var4[var6];
            tagNodeList.add((TagNode)obj);
        }

        return tagNodeList;
    }

    private static Object[] getByXPath(String content, String xpath) {
        HtmlCleaner htmlCleaner = new HtmlCleaner();
        TagNode html = htmlCleaner.clean(content);
        return evaluateXPath(xpath, html);
    }

    public static String parseText(String content, String xpath) {
        Object[] children = getByXPath(content, xpath);
        if(null != children && children.length > 0) {
            TagNode tagNode = (TagNode)children[0];
            return tagNode.getText().toString().trim();
        } else {
            return "";
        }
    }

    public static String parseText(TagNode tagNode, String xpath) {
        Object[] children = evaluateXPath(xpath, tagNode);
        if(null != children && children.length > 0) {
            Object tag = children[0];
            return ((TagNode)tag).getText().toString().trim();
        } else {
            return "";
        }
    }

    public static String parseAttribute(String content, String xpath, String attribute) {
        Object[] children = getByXPath(content, xpath);
        if(null != children && children.length > 0) {
            TagNode tagNode = (TagNode)children[0];
            return tagNode.getAttributeByName(attribute).trim();
        } else {
            return "";
        }
    }

    public static String parseAttribute(TagNode tagNode, String xpath, String attribute) {
        Object[] children = evaluateXPath(xpath, tagNode);
        if(null != children && children.length > 0) {
            Object tag = children[0];
            return ((TagNode)tag).getAttributeByName(attribute);
        } else {
            return "";
        }
    }

    public static String parseAttribute(TagNode tagNode, String attribute) {
        return tagNode.getAttributeByName(attribute).trim();
    }

    private static Object[] evaluateXPath(String xpath, TagNode html) {
        try {
            return html.evaluateXPath(xpath);
        } catch (XPatherException var3) {
            var3.printStackTrace();
            return new Object[0];
        }
    }
}