package org.example.mvc;

import org.example.mvc.annotation.Controller;
import org.example.mvc.annotation.RequestMapping;
import org.example.mvc.controller.RequestMethod;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping{

    private final Object[] basePackage;

    private Map<HandlerKey,AnnotationHandler> handlers = new HashMap<>();   //초기화하고

    public AnnotationHandlerMapping(Object...basePackage){
        this.basePackage = basePackage;
    }

    public void initialize(){
        Reflections reflections = new Reflections(basePackage); //구체적인 클래스 타입을 알지 못해도 그 클래스의 메소드, 타입, 변수들에 접근할 수 있도록 해주는 자바 API

        // HomeController
        Set<Class<?>> clazzesWithControllerAnnotation = reflections.getTypesAnnotatedWith(Controller.class); //@컨트롤러가 붙어있는 클래스들만 가져오기

        clazzesWithControllerAnnotation.forEach(clazz ->
                Arrays.stream(clazz.getDeclaredMethods()).forEach(declaredMethod->{
                            RequestMapping requestMapping = declaredMethod.getDeclaredAnnotation(RequestMapping.class);  //@RequestMapping이 있는 대상 추출

                            // @RequestMapping(value = "/",method = RequestMethod.GET)
                            Arrays.stream(getRequestMethods(requestMapping))
                                    .forEach(requestMethod -> handlers.put(
                                            new HandlerKey(requestMethod, requestMapping.value()),new AnnotationHandler(clazz,declaredMethod)
                                            //requestMethod -> RequestMethod.GET,    requestMapping.value()  -> "/"
                                    ));
                        }
                        ));
    }

    private RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        return requestMapping.method();
    }

    @Override
    public Object findHandler(HandlerKey handlerKey){
        return handlers.get(handlerKey);
    }
}
