package com.webserver.core;

import com.webserver.servlet.HttpServlet;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerContext {
    private static Map<String, HttpServlet> servletMapping=new HashMap<>();
    static{
        initmapping();
    }
    private static void initmapping(){

        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read("./config/web1.xml");
            Element root = document.getRootElement();
            List<Element> list = root.elements("servlet");
            for(Element e:list){
                String path=e.attributeValue("path");
                String className=e.attributeValue("className");
                Class cls = Class.forName(className);
                HttpServlet servlet = (HttpServlet) cls.newInstance();
                servletMapping.put(path,servlet);
            }
            System.out.println(servletMapping);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static HttpServlet getServletMapping(String name){
        return servletMapping.get(name);
    }



}
