package com.webserver.servlet;

import http.HttpRequest;
import http.HttpResponse;

public abstract class HttpServlet {
    public abstract void service(HttpRequest request, HttpResponse response);
}
