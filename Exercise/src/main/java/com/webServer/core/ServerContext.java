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
        initServerMapping();
    }

    private static void initServerMapping(){

        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read("./config/web1.xml");
            Element root = doc.getRootElement();
            List<Element> list = root.elements("servlet");
            for(Element e : list){
                String path = e.attributeValue("path");
                String className = e.attributeValue("className");
                Class cls = Class.forName(className);
                HttpServlet servlet = (HttpServlet) cls.newInstance();
                servletMapping.put(path,servlet);

            }
            System.out.println(servletMapping);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static HttpServlet getServlet(String doc){
        return servletMapping.get(doc);
    }

}
