package com.webserver.core;

import com.webserver.servlet.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 该类保存所有服务端共用信息
 */
public class ServerContext {
    private static Map<String, HttpServlet> servletMapping = new HashMap<>();
    static {
        servletMapping.put("/myweb/regUser",new RegServlet());
        servletMapping.put("/myweb/loginUser",new LoginServlet());
        servletMapping.put("/myweb/showAllUser",new ShowAllUserServlet());
        servletMapping.put("/myweb/passwordUser",new ChangePassword());
    }

    private static void initServletMapping(){

    }

    /**
     * 根据请求路径获取对应的Servlet
     * @param path
     * @return
     */

    public  static HttpServlet getServlet(String path){
        return  servletMapping.get(path);
    }


}
