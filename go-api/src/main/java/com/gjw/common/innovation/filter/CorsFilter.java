package com.gjw.common.innovation.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 跨域配置
 *
 * @author gaojunwei
 * @date 2019/10/14 18:18
 */
@WebFilter(filterName = "corsFilter", urlPatterns = "/*")
@Component
@Slf4j
public class CorsFilter implements Filter {

    static Set<String> allowedOrigins= new HashSet<>();
    static {
        allowedOrigins.add("http://plc.jd.com");
        allowedOrigins.add("https://plc.jd.com");

        allowedOrigins.add("http://nbr.jd.com");
        allowedOrigins.add("https://nbr.jd.com");

        allowedOrigins.add("http://wu.jd.com");
        allowedOrigins.add("https://wu.jd.com");

        allowedOrigins.add("http://test.jdr.jd.com");
        allowedOrigins.add("https://test.jdr.jd.com");

        allowedOrigins.add("http://pb.jd.com");
        allowedOrigins.add("https://pb.jd.com");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String originHeader = request.getHeader("Origin");
        if(allowedOrigins.contains(originHeader)){
            response.setHeader("Access-Control-Allow-Origin", originHeader);
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Headers", "X-Requested-With,content-type,token");
            response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
        }
        filterChain.doFilter(request, response);
    }
}