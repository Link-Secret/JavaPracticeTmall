package tmall.servlet;

import com.sun.deploy.net.HttpResponse;
import tmall.bean.Order;
import tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public class OrderServlet extends BaseBackServlet {
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        /*前台提供*/
        return null;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        /*前台提供*/
        return null;
    }

    public String delivery(HttpServletRequest request, HttpServletResponse response,Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Order o = orderDAO.get(id);
        o.setDeliveryDate(new Date());
        o.setStatus(orderDAO.waitConfirm);
        orderDAO.update(o);
        return "@admin_order_list";
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {

        return null;
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        List<Order> os = orderDAO.list(page.getStart(),page.getCount());
        orderItemDAO.fill(os);

        page.setTotal(page.getTotal());

        request.setAttribute("os",os);
        request.setAttribute("page",page);
        return "admin/listOrder.jsp";
    }
}