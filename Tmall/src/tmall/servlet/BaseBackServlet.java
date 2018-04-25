package tmall.servlet;

import tmall.dao.*;
import tmall.util.Page;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * \* Created with IntelliJ IDEA.
 * \* Author: acer zjl
 * \* Date: 2018/4/23 20:14
 * \* Description:
 * \
 */
public abstract class BaseBackServlet extends HttpServlet {

    public abstract String add(HttpServletRequest request, HttpServletResponse response, Page page) ;
    public abstract String delete(HttpServletRequest request, HttpServletResponse response, Page page) ;
    public abstract String edit(HttpServletRequest request, HttpServletResponse response, Page page) ;
    public abstract String update(HttpServletRequest request, HttpServletResponse response, Page page) ;
    public abstract String list(HttpServletRequest request, HttpServletResponse response, Page page) ;

    protected CategoryDAO categoryDAO = new CategoryDAO();
    protected OrderDAO orderDAO = new OrderDAO();
    protected OrderItemDAO orderItemDAO = new OrderItemDAO();
    protected ProductDAO productDAO = new ProductDAO();
    protected ProductImageDAO productImageDAO = new ProductImageDAO();
    protected PropertyDAO propertyDAO = new PropertyDAO();
    protected PropertyValueDAO propertyValueDAO = new PropertyValueDAO();
    protected ReviewDAO reviewDAO = new ReviewDAO();
    protected UserDAO userDAO = new UserDAO();

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) {
        try {
            /*获取分页信息，如果没有则依据默认设定*/
            int start = 0;
            int count = 0;
            try {
                start = Integer.parseInt(request.getParameter("page.start"));
            } catch (Exception e) {
                /*只catch不处理*/
            }
            try {
                count = Integer.parseInt(request.getParameter("page.count"));
            } catch (Exception e) {

            }

            /*page信息初始化完成*/
            Page page = new Page(start,count);
            /*通过反射调用对应的方法*/
            String method = (String) request.getAttribute("method");
            Method m = this.getClass().getMethod(method,HttpServletRequest.class,HttpServletResponse.class,Page.class);

            /*返回的字符串*/
            String redirect = m.invoke(this,request,response,page).toString();

            /*根据返回的字符串进行客户端或者服务端跳转*/
            if(redirect.startsWith("@")) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
