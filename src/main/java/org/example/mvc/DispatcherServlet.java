package org.example.mvc;

import org.example.mvc.controller.Controller;
import org.example.mvc.controller.RequestMethod;
import org.example.mvc.view.JspViewResolver;
import org.example.mvc.view.View;
import org.example.mvc.view.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
    private  static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private RequestMappingHandlerMapping rmhm;

    private List<ViewResolver> viewResolvers;

    @Override
    public void init() throws ServletException {
        rmhm = new RequestMappingHandlerMapping();
        rmhm.init();

        viewResolvers = Collections.singletonList(new JspViewResolver());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("[DispatcherServlet] Service started");

        try {
            //Request.getMethod 를 하면 get인지 post 인지 알수있고
            Controller handler = rmhm.findHandler(new HandlerKey(RequestMethod.valueOf(request.getMethod()),request.getRequestURI()));
            // viewname이 "redirect:/users"로 인식
            String viewName = handler.handleRequest(request,response);
            System.out.println("viewName11111111111:"+viewName); //     /user/list

            for (ViewResolver viewResolver : viewResolvers) {
                View view = viewResolver.resolverView(viewName);
                view.render(new HashMap<>(),request,response);
            }

            System.out.println("viewName2222222222222:"+viewName); //     /user/list.jsp

//            RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
//            requestDispatcher.forward(request,response);

        } catch (Exception e) {
            log.error("exception occurred: [{}] ",e.getMessage(),e);
            throw new ServletException(e);
        }
    }
}