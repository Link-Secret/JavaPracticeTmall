package tmall.servlet;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmall.dao.CategoryDAO;
import tmall.dao.OrderDAO;
import tmall.dao.OrderItemDAO;
import tmall.dao.ProductDAO;
import tmall.dao.ProductImageDAO;
import tmall.dao.PropertyDAO;
import tmall.dao.PropertyValueDAO;
import tmall.dao.ReviewDAO;
import tmall.dao.UserDAO;
import tmall.util.Page;

public class BaseForeServlet extends HttpServlet{

    protected CategoryDAO categoryDAO = new CategoryDAO();
    protected OrderDAO orderDAO = new OrderDAO();
    protected OrderItemDAO orderItemDAO = new OrderItemDAO();
    protected ProductDAO productDAO = new ProductDAO();
    protected ProductImageDAO productImageDAO = new ProductImageDAO();
    protected PropertyDAO propertyDAO = new PropertyDAO();
    protected PropertyValueDAO propertyValueDAO = new PropertyValueDAO();
    protected ReviewDAO reviewDAO = new ReviewDAO();
    protected UserDAO userDAO = new UserDAO();

    public void service(HttpServletRequest request, HttpServletResponse response) {
        try {
            /*默认分页规则*/
            int start = 0;
            int count = 10;
            /*如果修改，则按照修改后规则*/
            try {
                start = Integer.parseInt(request.getParameter("page.start"));
            } catch (Exception e) {

            }
            try {
                count = Integer.parseInt(request.getParameter("page.count"));
            } catch (Exception e) {

            }
            Page page = new Page(start,count);

            /*根据反射访问对应的方法*/
            String method = (String) request.getAttribute("method");
            Method m = this.getClass().getMethod(method,HttpServletRequest.class,
                                HttpServletResponse.class,Page.class);
            String redirect = m.invoke(this,request,response,page).toString();

            /*即在这里，先跳转到对应的反射方法里面去，执行完方法就返回到这里*/

            /*根据home方法的返回值"home.jsp"，即没有"%"开头，也没有"@",那么就调用
            request.getRequestDispatcher(redirect).forward(request, response);
            进行服务端跳转到 "home.jsp" 页面*/
            /*根据对应方法的返回值，进行服务端跳转、客户端跳转、或者直接输出字符串*/
            if(redirect.startsWith("@")){
                response.sendRedirect(redirect.substring(1));
            }else if(redirect.startsWith(("%"))){
                response.getWriter().print(redirect.substring(1));
            }else {
                request.getRequestDispatcher(redirect).forward(request,response);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}