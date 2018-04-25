package tmall.filter;

import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * \* Created with IntelliJ IDEA.
 * \* Author: acer zjl
 * \* Date: 2018/4/23 20:27
 * \* Description:
 * \
 */
public class BackServletFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        /*获取应用的路径contextpath   /tmall*/
        String contextPath = request.getServletContext().getContextPath();
        /*获取URI /tmall/admin_category_list*/
        String uri = request.getRequestURI();
        uri = StringUtils.remove(uri,contextPath);
        if(uri.startsWith("/admin_")) {
            /*字符串添加Servlet从而调用对应的Servlet,对应的方法*/
            String servletPath = StringUtils.substringBetween(uri,"_","_") + "Servlet";
            String method = StringUtils.substringAfterLast(uri,"_");

            /*转发到对应的Servlet*/
            /*此时如果有带参数，直接通过request.getparameter调用*/
            request.setAttribute("method",method);
            servletRequest.getRequestDispatcher("/"+servletPath).forward(request,response);
            return;
        }

    }

    @Override
    public void destroy() {

    }
}
