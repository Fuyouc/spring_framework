package org.spring.core.parser.xml;

import java.io.InputStream;

/**
 * 解析XML文件
 */
public interface XMLParser {
    /**
     * 解析XML,返回root元素
     */
    Element parser(InputStream inputStream);
}
