package com.example.chat.filter;

import cn.hutool.json.JSONUtil;
import com.example.chat.component.WebSocketServer;
import com.example.chat.pojo.Result;
import com.example.chat.pojo.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebFilter(urlPatterns = "/*")
public class LoginFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)  servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestUrl = request.getRequestURL().toString();
        if (request.getMethod().equals("OPTIONS")){
            filterChain.doFilter(servletRequest,servletResponse);
        }
        if (requestUrl.contains("/chat/server")){
            Map<String, String[]> parameterMap = request.getParameterMap();
            log.info("socket连接进行验证");
            if (parameterMap.isEmpty()) return;
            String token = parameterMap.get("token")[0];
            try{
                JwtUtil.parseJWT(token);
                filterChain.doFilter(servletRequest,servletResponse);
            }catch (Exception e){
                return;
            }
            return;
        }

        //路径白名单
        List<String> excludePath = new ArrayList<>();
        excludePath.add("/chat/user/register");
        excludePath.add("/chat/user/login");
        excludePath.add("/chat/download");
        for (String path : excludePath){
            if (requestUrl.contains(path)){
                filterChain.doFilter(servletRequest,servletResponse);
                return;
            }
        }
        String jwt = request.getHeader("token");
        log.info("token={}",jwt);
        if(!StringUtils.hasLength(jwt)){
            log.info("{}未找到token",requestUrl);
            Result error = Result.error("NOT_LOGIN");
            //手动转换 对象--json ----- > fastJSON
            String notLogin = JSONUtil.toJsonStr(error);
            response.getWriter().write(notLogin);
            return;
        }
        try{
            JwtUtil.parseJWT(jwt);
        }catch (Exception e){
            log.info("{}，token过时",requestUrl);
            Result error = Result.error("TOKEN_PAST");
            String notLogin = JSONUtil.toJsonStr(error);
            response.getWriter().write(notLogin);
            return;
        }
        log.info("{}，token通过",requestUrl);
        filterChain.doFilter(servletRequest,servletResponse);

    }
}
