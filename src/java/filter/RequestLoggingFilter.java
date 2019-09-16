package filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RequestLoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        String requestURI = ((HttpServletRequest) servletRequest).getRequestURI();
        String method = ((HttpServletRequest) servletRequest).getMethod();
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println(method + "\n" + " RequestURI: " + requestURI + "\n" + " Milliseconds used: " + (System.currentTimeMillis() - start));

    }

    @Override
    public void destroy() {

    }
}