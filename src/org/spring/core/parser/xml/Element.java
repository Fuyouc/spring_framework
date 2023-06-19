package org.spring.core.parser.xml;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 每个XML元素（标签对象）
 */
public interface Element {

    /**
     * 添加标签中的文字
     */
    void addContext(String context);

    /**
     * 获取元素名称
     */
    String getName();

    /**
     * 获取标签上的属性值
     */
    String getAttributes(String name);

    /**
     * 如果需要子标签包裹的内容，则将标签放入即可
     */
    void need(Element child);

    /**
     * 获取标签的内容
     */
    String buildContext();

    /**
     * 获取属性集合
     */
    Map<String,String> getAttributes();

    /**
     * 获取父元素
     * @return
     */
    Element getParent();

    /**
     * 获取指定的child
     */
    Element getChild(String labelName,String id);

    /**
     * 获取指定子标签列表
     */
    List<Element> getChild(String labelName);

    /**
     * 获取所有的子元素集合
     */
    Map<String, Map<String,Element>>  getChild();
}
