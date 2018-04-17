package tmall.filter;

import org.apache.commons.lang.StringUtils;
import tmall.bean.Category;
import tmall.bean.OrderItem;
import tmall.bean.User;
import tmall.dao.CategoryDAO;
import tmall.dao.OrderItemDAO;

import javax.servlet.*;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ForeServletFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        /*HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

      *//*localhost/tmall/forehome*//*
        *//*//*tmall*//*
        String contextPath = request.getServletContext().getContextPath();
        *//*在servlet对象中设置一个attribute*//*
        request.getServletContext().setAttribute("contextPath",contextPath);

        *//*用于当用户登录之后，从session中获取用户对象，并根据这个用户对象获取购物车中的物品总数。*//*
        User user = (User) request.getSession().getAttribute("user");
        int cartTotalItemNumber = 0;
        if(null != user){
            List<OrderItem> ois = new OrderItemDAO().listByUser(user.getId());
            for(OrderItem oi :ois) {
                cartTotalItemNumber += oi.getNumber();
            }
        }
        request.setAttribute("cartTotalItemNumber",cartTotalItemNumber);

        *//*getAttribute返回的是一个Object类型*//*
        List<Category> cs = (List<Category>) request.getAttribute("cs");
        if(null == cs) {
            cs = new CategoryDAO().list();
            request.setAttribute("cs",cs);
        }

        *//*URI为 /tmall/forehome*//*
        String uri = request.getRequestURI();
        *//*得到/forehome*//*
        uri = StringUtils.remove(uri,contextPath);

        if(uri.startsWith("/fore")&&!uri.startsWith("/foreServlet")) {
            String  method = StringUtils.substringAfterLast(uri,"/fore");
            request.setAttribute("method",method);
            servletRequest.getRequestDispatcher("/foreServlet").forward(request,response);
            return;
        }
        chain.doFilter(request,response);*/

        /*将servlet类型的向下强转为httpservlet类型*/
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        /*通过路径地址得到对应的方法*/
        /*/tmall*/
        String contextPath = request.getServletContext().getContextPath();
        request.getServletContext().setAttribute("contextPath",contextPath);

        User user = (User) request.getSession().getAttribute("user");
        int cartTobleItemNumber = 0;
        if(null != user) {
            /*得到当前用户的订单项集合*/
            List<OrderItem> ois = new OrderItemDAO().listByUser(user.getId());
            /*遍历每个订单项，得到购物的总数*/
            for(OrderItem oi : ois) {
                cartTobleItemNumber += oi.getNumber();
            }
        }
        request.setAttribute("cartTobleItemNumber",cartTobleItemNumber);

        List<Category> cs = (List<Category>) request.getAttribute("cs");
        if(null == cs) {
            cs = new CategoryDAO().list();
            request.setAttribute("cs",cs);
        }

        /*/tmall/forehome*/
        String uri = request.getRequestURI();
        /*/forehome*/
        uri = StringUtils.remove(uri,contextPath);
        if(uri.startsWith("/fore") && !uri.startsWith("/foreServlet")) {
            /*通过反射调用对应的方法*/
            String method = StringUtils.substringAfterLast(uri,"/fore");
            /*将方法放在request中*/
            request.setAttribute("method",method);

            /*转发：服务器端跳转*/
            servletRequest.getRequestDispatcher("/foreServlet").forward(request,response);
            return;
        }
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}