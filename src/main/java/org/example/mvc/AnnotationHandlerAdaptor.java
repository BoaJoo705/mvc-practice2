package org.example.mvc;

import org.example.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdaptor implements HandlerAdaptor{
    @Override
    public boolean supports(Object handler) {
        return handler instanceof AnnotationHandler; // 핸들러에서 어노테이션핸들러라면 하단 햄들러 메소드 실행
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String viewName = ((AnnotationHandler) handler).handle(request, response);
        return new ModelAndView(viewName);
    }
}
