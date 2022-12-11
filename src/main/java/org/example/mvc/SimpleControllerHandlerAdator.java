package org.example.mvc;

import org.example.mvc.controller.Controller;
import org.example.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleControllerHandlerAdator implements HandlerAdaptor{
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof Controller); // 객체 instanceof 클래스(객체 타입을 확인하는 연산자) //특정 타입임을 알아내고 특정 코드를 실행하기 위해서
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception {
        String viewName = ((Controller) handler).handleRequest(request, response);
        System.out.println("viewName:"+viewName);    //user/form     , /user/list,home
        return new ModelAndView(viewName);
    }
}
