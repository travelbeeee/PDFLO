package travelbeeee.PDFLO.web.intercepter;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginIntercepter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String queryString = request.getQueryString();
        if (queryString != null) {
            requestURI = requestURI + "?" + queryString;
        }

        HttpSession session = request.getSession(false);

        if(session == null || session.getAttribute("id") == null){
            response.sendRedirect("/member/login?redirectURL=" + requestURI);
            return false;
        }
        return true;
    }
}

