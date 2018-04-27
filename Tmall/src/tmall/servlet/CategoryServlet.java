package tmall.servlet;

import tmall.bean.Category;
import tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* Author: acer zjl
 * \* Date: 2018/4/23 20:50
 * \* Description:
 * \
 */
public class CategoryServlet extends BaseBackServlet{
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
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
        List<Category> cs = categoryDAO.list(page.getStart(),page.getCount());

        /*total值决定分页*/
        int total = categoryDAO.getTotal();
        page.setTotal(total);

        /*将分类集合cs,分页信息放在page中*/
        request.setAttribute("thecs",cs);
        request.setAttribute("page",page);

        /*因为浏览器的地址不会改变所以只进行服务端跳转*/
        return "admin/listCategory.jsp";
    }
}
