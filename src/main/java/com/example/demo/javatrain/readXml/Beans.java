package com.example.demo.javatrain.readXml;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MiaoShaoDong
 * @date 14:07 2020/11/2
 */
public class Beans implements IBeans {
    private static Map<String, Object> banes = new HashMap<>();

    public Beans(String configXml) throws Exception {
        //创建SAXReader对象
        SAXReader reader = new SAXReader();
        //xml文件的位置
        URL sources = Beans.class.getClassLoader().getResource(configXml);
        //创建document对象,并读取xml文件 （解析xml文件）
        Document document =  reader.read(sources);
        //读取元素
        Element ele = document.getRootElement();
        ele = ele.element("SNMP");
        this.getNodes(ele);
    }


    /**
     * 从指定节点开始,递归遍历所有子节点
     * @author chenleixing
     */
    public void getNodes(Element node){
        System.out.println("--------------------");

        //当前节点的名称、文本内容和属性
        System.out.println("当前节点名称："+node.getName());//当前节点名称
        System.out.println("当前节点的内容："+node.getTextTrim());//当前节点名称
        List<Attribute> listAttr=node.attributes();//当前节点的所有属性的list
        for(Attribute attr:listAttr){//遍历当前节点的所有属性
            String name=attr.getName();//属性名称
            String value=attr.getValue();//属性的值
            System.out.println("属性名称："+name+"属性值："+value);
        }

        //递归遍历当前节点所有的子节点
        List<Element> listElement=node.elements();//所有一级子节点的list
        for(Element e:listElement){//遍历所有一级子节点
            this.getNodes(e);//递归
        }
    }
    @Override
    public Object getBean(String name) {
        return banes.get(name);
    }
}
