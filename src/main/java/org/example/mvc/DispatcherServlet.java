package org.example.mvc;

import org.example.mvc.controller.Controller;
import org.example.mvc.controller.RequestMethod;
import org.example.mvc.view.JspViewResolver;
import org.example.mvc.view.ModelAndView;
import org.example.mvc.view.View;
import org.example.mvc.view.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;


@WebServlet("/")
public class DispatcherServlet extends HttpServlet {
    private  static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings; // 핸들러매핑

    private List<HandlerAdaptor> handlerAdaptors; // 핸들어댑터

    private List<ViewResolver> viewResolvers; //뷰리졸버

    @Override
    public void init() throws ServletException { //초기화
        RequestMappingHandlerMapping rmhm = new RequestMappingHandlerMapping();
        rmhm.init();

        // 다른 코드는 건드리지 않고 애노테이션 추가
        AnnotationHandlerMapping ahm = new AnnotationHandlerMapping("org.example");
        ahm.initialize();

        handlerMappings = List.of(rmhm,ahm);
        // 다른 코드는 건드리지 않고 애노테이션 추가
        handlerAdaptors = List.of(new SimpleControllerHandlerAdator(), new AnnotationHandlerAdaptor()); //애노테이션 핸들러 어댑터를 따로 추가해줘야한다.
        viewResolvers = Collections.singletonList(new JspViewResolver());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("[DispatcherServlet] Service started");
        String requestURI = request.getRequestURI();
        RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

        try {
            //Request.getMethod 를 하면 get인지 post 인지 알수있고
//            Object handler = hm.findHandler(new HandlerKey(RequestMethod.valueOf(request.getMethod()),request.getRequestURI()));  // 핸들러매핑을 검색해서 핸들을 찾고 핸들은 컨트롤러르 의미함
            // viewname이 "redirect:/users"로 인식
//            String viewName = handler.handleRequest(request,response);
            Object handler = handlerMappings.stream()// 핸들러 매핑에서 핸들러를 가지고옴
                    .filter(hm ->hm.findHandler(new HandlerKey(requestMethod,requestURI)) !=null)
                    .map(hm->hm.findHandler(new HandlerKey(requestMethod,requestURI)))
                    .findFirst()
                    .orElseThrow(() -> new ServletException("No handler for[" + requestMethod+", " + requestURI +"]"));

//            findHandler(new HandlerKey(RequestMethod.valueOf(request.getMethod()),request.getRequestURI()));

            HandlerAdaptor handlerAdaptor = handlerAdaptors.stream()     // 핸들러를 지원하는 어댑터를 찾아서 //스트림(Streams)은 람다를 활용할 수 있는 기술 중 하나
                    .filter(ha -> ha.supports(handler))                     // 핸들러 어댑터를 통해 어댑터를 가지고 와서
                    .findFirst()
                    .orElseThrow(() -> new ServletException("No adaptor for handler [" + handler + "]"));

            // 내부에서 핸들러를 전달해서
            ModelAndView modelAndView = handlerAdaptor.handle(request,response,handler); //핸들러 어댑터를 실행해줌 (핸들러를 전달해주면) 모델앤뷰를 돌려주고 //home,


            for (ViewResolver viewResolver : viewResolvers) {
                View view = viewResolver.resolverView(modelAndView.getViewName()); //뷰 리졸버에서 뷰를 선택해서
//                view.render(new HashMap<>(),request,response);
                view.render(modelAndView.getModel(),request,response); //뷰를 랜더링해주면
                System.out.println("view:"+view); //     /user/list.jsp
            }


//            RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
//            requestDispatcher.forward(request,response);

        } catch (Exception e) {
            log.error("exception occurred: [{}] ",e.getMessage(),e);
            throw new ServletException(e);
        }
    }
}