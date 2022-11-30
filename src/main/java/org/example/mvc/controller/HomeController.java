package org.example.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeController implements Controller{
    public String handleRequest(HttpServletRequest request , HttpServletResponse response) throws Exception{
        return "home.jsp";
    }
}
