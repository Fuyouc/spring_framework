package org.spring.core.parser.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

public class SpringXmlParser implements XMLParser{
    @Override
    public Element parser(InputStream inputStream) {
        try {
            XMLElement root = new XMLElement();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(inputStream,new SpringXMLHandler(root));
            return root;
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class SpringXMLHandler extends DefaultHandler{
        private XMLElement parentElement;
        private XMLElement currentElement;
        Stack<XMLElement> stack;
        private String currentName;
        private String rootName;
        private boolean first = true;


        public SpringXMLHandler(XMLElement root) {
            this.currentElement = root;
            stack = new Stack<>();
        }

        @Override
        public void startElement(String uri, String localName, String labelName, Attributes attributes) throws SAXException {
            currentName = labelName;
            if (first){
                rootName = labelName;
                currentElement.setName(labelName);
                if (attributes != null){
                    for (int i = 0; i < attributes.getLength(); i++) {
                        currentElement.addAttributes(attributes.getQName(i),attributes.getValue(i));
                    }
                }
                first = false;
            }else {
                XMLElement newElement = new XMLElement(currentElement);
                newElement.setName(labelName);
                if (attributes != null){
                    for (int i = 0; i < attributes.getLength(); i++) {
                        newElement.addAttributes(attributes.getQName(i),attributes.getValue(i));
                    }
                }
                currentElement.addChild(labelName,newElement);
                stack.add(currentElement);
                parentElement = currentElement;
                currentElement = newElement;

                if (parentElement != null && !parentElement.getName().equals(currentName)){
                    /**
                     * 当前元素在文本内容后，会自动在父元素相对的位置使用 ${标签名称-编号} 的形式来当做占位符
                     * 使用need()方法传入对应的元素，即可把占位符替换成实际的文本内容
                     */
                    parentElement.addContext(" @{"+currentElement.getName() + parentElement.getChild(currentElement.getName()).size()+"}");
                }

            }
        }

        @Override
        public void endElement(String uri, String localName, String labelName) throws SAXException {
            //到达当前最后一个标签时回调
            if (labelName.equals(currentName)){
                if (!stack.isEmpty()) {
                    currentElement = stack.pop();
                    currentName = currentElement.getName();
                    parentElement = (XMLElement) currentElement.getParent();
                }
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            String text = new String(ch, start, length).trim();
            //标签中的内容
            currentElement.addContext(text);
        }
    }
}
