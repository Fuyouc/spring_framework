package org.spring.jdbc.xml;

import org.spring.annotations.autoconfig.Component;
import org.spring.core.container.files.SpringFile;
import org.spring.core.handler.SpringFileHandler;
import org.spring.core.parser.xml.Element;
import org.spring.core.parser.xml.SpringXmlParser;
import org.spring.core.parser.xml.XMLParser;
import org.spring.utils.global.ObjectUtils;

import javax.management.modelmbean.XMLParseException;
import javax.print.attribute.standard.MediaSize;

/**
 * 处理dao接口xml文件
 */
@Component
public class DaoXMLMapperHandler implements SpringFileHandler {

    private XMLParser parser;
    private static final String BASE_LABEL = "mapper";

    public DaoXMLMapperHandler() {
        parser = new SpringXmlParser();
    }

    @Override
    public boolean handler(SpringFile file) {
        if (file.getName().contains(".xml") && file.getName().endsWith(".xml")){
            //如果是一个xml文件
            Element root = parser.parser(file.getInputStream());
            if (BASE_LABEL.equals(root.getName())){
                try {
                    //如果最外层是使用指定的标签，才进行处理
                    String namespace = root.getAttributes("namespace");
                    if (ObjectUtils.isEmpty(namespace)){
                        throw new XMLParseException("请在 " + file.getName() + " 中的<mapper>标签添加namespace");
                    }else {
                        if (DaoXMLMapperManager.containsDao(namespace)){
                            //如果有该类，则添加到管理器中
                            DaoXMLMapperManager.addXML(namespace,root);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }
}
