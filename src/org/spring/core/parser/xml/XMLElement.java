package org.spring.core.parser.xml;

import org.apache.tomcat.util.security.Escape;
import org.spring.utils.global.ObjectUtils;
import org.spring.utils.global.StringUtils;

import javax.management.modelmbean.XMLParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

public class XMLElement implements Element{

    private String name;
    private Map<String,String> attributes;
    private Map<String,Map<String,Element>> child;

    private Element parent;

    private StringBuilder sb;

    public XMLElement() {
        this(null);
    }

    public XMLElement(Element parent){
        this.parent = parent;
        attributes = new ConcurrentHashMap<>();
        child = new ConcurrentHashMap<>();
        sb = new StringBuilder();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addAttributes(String key,String value){
        attributes.put(key,value);
    }


    public void addChild(String labelName,Element element){
        try {
            String id = element.getAttributes("id");
            int index = 1;
            Map<String, Element> elementMap;
            if (child.containsKey(labelName)){
                elementMap = child.get(labelName);
                index = elementMap.size() + 1;
            }else {
                elementMap = new ConcurrentSkipListMap<>();
                child.put(labelName,elementMap);
            }
            if (ObjectUtils.isEmpty(id)){
                id = labelName + index;
            }
            elementMap.put(id,element);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void addContext(String context) {
        sb.append(context);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAttributes(String name) {
        return attributes.get(name);
    }

    @Override
    public void need(Element element) {
        Collection<Element> values = child.get(element.getName()).values();
        int index = 1;
        for (Element value : values) {
            if (value.equals(element)){
                String placeholder = element.getName() + (index);
                String context = sb.toString().replaceFirst("\\@\\{"+placeholder+"}",element.buildContext());
                sb.setLength(0);
                sb.append(context);
                break;
            }
            ++index;
        }
    }

    @Override
    public String buildContext() {
        return sb.toString().replaceAll("@\\{[^}]*\\}","");
    }

    @Override
    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public Element getParent() {
        return parent;
    }

    @Override
    public Element getChild(String labelName, String id) {
        if (child.containsKey(labelName)) {
            return child.get(labelName).get(id);
        }
        return null;
    }

    @Override
    public List<Element> getChild(String name) {
        if (child.containsKey(name)){
            return child.get(name).values().stream().collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Map<String, Map<String,Element>> getChild() {
        return child;
    }


    @Override
    public String toString() {
        return "XMLElement{" +
                "name='" + name + '\'' +
                ", attributes=" + attributes +
                ", child=" + child +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XMLElement that = (XMLElement) o;
        return Objects.equals(name, that.name) && Objects.equals(attributes, that.attributes) && Objects.equals(child, that.child) && Objects.equals(sb, that.sb);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, attributes, child, sb);
    }
}
