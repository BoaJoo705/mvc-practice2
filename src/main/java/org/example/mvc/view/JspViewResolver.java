package org.example.mvc.view;

import static org.example.mvc.view.RedirectView.DEFAULT_REDIRECT_PREFIX;

public class JspViewResolver implements ViewResolver{

    @Override
    public View resolverView(String viewName) {
        if(viewName.startsWith(DEFAULT_REDIRECT_PREFIX)){
            return new RedirectView(viewName); // redirectview 면 redirectview로 리턴
        }
        return new JspView(viewName + ".jsp");
    }
}
