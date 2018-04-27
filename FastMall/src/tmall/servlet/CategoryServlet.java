package tmall.servlet;

/*1.通过访问地址，admin_category_list,首先进入的就是web.xml的filter-mapping配置的先后顺序来进行拦截，顺序为EncodingFilter--》BackServletFilter-->之后的就是前端相关的FIlter

2.进入BackServletFilter（implements Filter接口)之后,进入dofilter方法，取得contextpath(/FastMall),取得uri(/FastMall/admin_category_list),字符串操作后判断是否以/admin开头，如果是则转发到/categoryServlet,并将method='list'放入request，return
如果不是/admin开头则进入下一个filter链

3.转发到/categoryServlet,会被web.xml中配置的url-pattern匹配到，从而进入categoryServlet中，由于categoryServlet继承自BaseBackServlet,所以首先调用父类（BaseBackServlet)重写的HttpServlet的service方法，service方法中先获取分页信息，如果为空则默认设置分页，接着利用反射调用method字符串所对应的方法

4.这时候才真正的进入了categoryServlet的对应的method，list方法获取Request，Response，page信息，通过处理将分页信息，分类集合放在Attribute中，return "admin/listCategory";

5.在BaseBackServlet中得到反射调用方法返回的字符串来进行相应的处理，如果是以@开头的字符串，则进行客户端跳转(url地址发生变化），%开头则只打印信息，其余情况则进行服务端跳转，上面进行的是服务器端跳转，所以地址依然为....../FastMall/admin_category_list，
6.跳转到admin/listCategoy中，则是展示信息了，根据categoryServlet放在Request中的Attribute，进行分页，并获取CS属性集合遍历从而展示信息
7.admin/listCategoy.jsp文件中，包含adminHeader.jsp, adminNavigator.jsp, adminPage.jsp, adminFooter.jsp四个公共文件，我们可以在include/admin中找到
8.该页面有几个功能，提供图片属性名等的展示，分页功能。还有编辑，删除，增加功能，剩余的就是产品管理，属性管理的连接
9.这里新增需要使用到文件上传功能，文件上传需要使用到util包的ImageUtil类，进行统一处理转换成 name.jpg
10.在listCateogy.jsp中点击链接都会调用对应的servlet的对应的方法，有的进行客户端跳转，有的进行服务器跳转，有的直接打印信息到页面*/



import java.awt.image.BufferedImage;	
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tmall.bean.Category;
import tmall.util.ImageUtil;
import tmall.util.Page;

public class CategoryServlet extends BaseBackServlet {
	
	@Override
	public String add(HttpServletRequest request, HttpServletResponse response, Page page) {
		Map<String,String> params = new HashMap<>();
		InputStream is = super.parseUpload(request, params);
		
		String name= params.get("name");
		Category c = new Category();
		c.setName(name);
		categoryDAO.add(c);
		
		File  imageFolder= new File(request.getSession().getServletContext().getRealPath("img/category"));
		File file = new File(imageFolder,c.getId()+".jpg");
		
		try {
			if(null!=is && 0!=is.available()){
			    try(FileOutputStream fos = new FileOutputStream(file)){
			        byte b[] = new byte[1024 * 1024];
			        int length = 0;
			        while (-1 != (length = is.read(b))) {
			            fos.write(b, 0, length);
			        }
			        fos.flush();
			        //通过如下代码，把文件保存为jpg格式
			        BufferedImage img = ImageUtil.change2jpg(file);
			        ImageIO.write(img, "jpg", file);		
			    }
			    catch(Exception e){
			    	e.printStackTrace();
			    }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return "@admin_category_list";
	}

	
	@Override
	public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		categoryDAO.delete(id);
		return "@admin_category_list";
	}

	
	@Override
	public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
		int id = Integer.parseInt(request.getParameter("id"));
		Category c = categoryDAO.get(id);
		request.setAttribute("c", c);
		return "admin/editCategory.jsp";		
	}

	
	@Override
	public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
		Map<String,String> params = new HashMap<>();
		InputStream is = super.parseUpload(request, params);
		
		System.out.println(params);
		String name= params.get("name");
		int id = Integer.parseInt(params.get("id"));

		Category c = new Category();
		c.setId(id);
		c.setName(name);
		categoryDAO.update(c);
		
		File  imageFolder= new File(request.getSession().getServletContext().getRealPath("img/category"));
		File file = new File(imageFolder,c.getId()+".jpg");
		file.getParentFile().mkdirs();
		
		try {
			if(null!=is && 0!=is.available()){
			    try(FileOutputStream fos = new FileOutputStream(file)){
			        byte b[] = new byte[1024 * 1024];
			        int length = 0;
			        while (-1 != (length = is.read(b))) {
			            fos.write(b, 0, length);
			        }
			        fos.flush();
			        //通过如下代码，把文件保存为jpg格式
			        BufferedImage img = ImageUtil.change2jpg(file);
			        ImageIO.write(img, "jpg", file);		
			    }
			    catch(Exception e){
			    	e.printStackTrace();
			    }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "@admin_category_list";

	}

	
	@Override
	public String list(HttpServletRequest request, HttpServletResponse response, Page page) {
		List<Category> cs = categoryDAO.list(page.getStart(),page.getCount());
		int total = categoryDAO.getTotal();
		page.setTotal(total);
		
		request.setAttribute("thecs", cs);
		request.setAttribute("page", page);
		
		return "admin/listCategory.jsp";
	}
}
