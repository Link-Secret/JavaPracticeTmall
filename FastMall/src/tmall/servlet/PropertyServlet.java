package tmall.servlet;

import tmall.bean.Category;
import tmall.bean.Property;
import tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class PropertyServlet extends BaseBackServlet {
    @Override
    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category c = categoryDAO.get(cid);

        Property p = new Property();
        String name = request.getParameter("name");
        p.setName(name);
        p.setCategory(c);
        /*add方法:insert into Property values(null,?,?)*/
        propertyDAO.add(p);

        return "@admin_property_list?cid="+cid;
    }

    @Override
    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        int id = Integer.parseInt(request.getParameter("id"));
        Property p = propertyDAO.get(id);
        /*delete方法:delete from Property where id = " + id;*/
        propertyDAO.delete(id);
        return "@admin_property_list?cid="+p.getCategory().getId();
    }

    @Override
    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        /*edit方法先跳转到editProperty.jsp页面，再跳转到update方法*/
        int id = Integer.parseInt(request.getParameter("id"));
        Property p = propertyDAO.get(id);
        request.setAttribute("p",p);
        return "admin/editProperty.jsp";
    }

    @Override
    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        /*从editProperty.jsp中传来cid ,id*/
        int cid = Integer.parseInt(request.getParameter("cid"));
        int id = Integer.parseInt(request.getParameter("id"));

        Category c = categoryDAO.get(cid);
        /*不是很清楚为什么不是propertyDAO.get(id)
        * 测试结果：都可以
        * */
        Property p = new Property();
        String name = request.getParameter("name");
        p.setName(name);
        p.setId(id);
        p.setCategory(c);
        propertyDAO.update(p);

        /*update Property set cid= ?, name=? where id = ?*/
        /*+p.getCategory.getId()*/
        return "@admin_property_list?cid="+cid;
    }

    @Override
    public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));

        /*select * from Category order by id desc limit ?,? */
        Category c = categoryDAO.get(cid);
        List<Property> ps = propertyDAO.list(cid,page.getStart(),page.getCount());

        /*select count(*) from Property where cid =*/
        int total = propertyDAO.getTotal(cid);
        page.setTotal(total);
        page.setParam("&cid="+c.getId());

        request.setAttribute("ps",ps);
        request.setAttribute("c",c);
        request.setAttribute("page",page);

        /*select * from Property where cid = ? order by id desc limit ?,?*/
        return "admin/listProperty.jsp";
    }
}
