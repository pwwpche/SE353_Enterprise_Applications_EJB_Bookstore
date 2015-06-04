package Servlets.Filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * Created by pwwpche on 2015/4/25.
 */
@WebFilter(filterName = "XSSFilter", urlPatterns = "/chatRoom.jsp")
public class XssFilter implements Filter {
    @SuppressWarnings("unused")
    private FilterConfig filterConfig;

    public void destroy() {
        this.filterConfig = null;
    }
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        chain.doFilter(new XssRequestWrapper((HttpServletRequest) request), response);
    }
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }
}

