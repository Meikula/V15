package com.webServer.core;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import servlet.HttpServlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerContext {
    private static Map<String, HttpServlet> servletMapping = new HashMap<>();
    static{
        initmap();
    }
    private static void initmap(){
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read("./config/web1.xml");
            Element root = document.getRootElement();
            List<Element> list = root.elements("servlet");
            for(Element e:list){
                String path=e.attributeValue("path");
                String className = e.attributeValue("className");
                Class cls = Class.forName(className);
                HttpServlet servlet = (HttpServlet)cls.newInstance();
                servletMapping.put(path,servlet);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HttpServlet getMapping(String name){
        return servletMapping.get(name);
    }


}
