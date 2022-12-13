package org.example.mvc;

import org.example.mvc.controller.*;

import java.util.HashMap;
import java.util.Map;

public class RequestMappingHandlerMapping implements HandlerMapping{
    //key는 urlpath
    private Map<HandlerKey, Controller> mappings = new HashMap<>();

    void init(){ // 맵을 초기화
//        mappings.put(new HandlerKey(RequestMethod.GET,"/"),new HomeController());
        mappings.put(new HandlerKey(RequestMethod.GET,"/users"),new UserListController());
        mappings.put(new HandlerKey(RequestMethod.POST,"/users"),new UserCreateController());
        mappings.put(new HandlerKey(RequestMethod.GET,"/user/form"),new ForwardController("/user/form"));
    }

    public Controller findHandler(HandlerKey handlerKey){ // 키에 맞는 컨트롤러 값 찾기
        return mappings.get(handlerKey);
    }
}
