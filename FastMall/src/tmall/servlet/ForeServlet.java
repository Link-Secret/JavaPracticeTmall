package tmall.servlet;

import com.sun.org.apache.xpath.internal.operations.Or;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.web.util.HtmlUtils;
import tmall.bean.*;
import tmall.comparator.*;
import tmall.dao.*;
import tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ForeServlet extends BaseForeServlet {

    public String home(HttpServletRequest request,HttpServletResponse response,Page page) {
        /*获取所有17种分类*/
        List<Category> cs = new CategoryDAO().list();
        /*为这些分类填充产品集合，即为每个Category对象，设置products属性*/
        new ProductDAO().fill(cs);
        /*为这些分类填充推荐产品集合，即为每个Category对象，设置productsByRow属性*/
        new ProductDAO().fillByRow(cs);
        /*把分类集合设置在request的"cs"属性上*/
        request.setAttribute("cs",cs);
        /*服务器端跳转*/
        return "home.jsp";
    }

    public String register(HttpServletRequest request,HttpServletResponse response,Page page) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        /*通过HtmlUtils.htmlEscape(name);把账号里的特殊符号进行转义*/
        name = HtmlUtils.htmlEscape(name);

        /*判断用户是否正确*/
        boolean exist = userDAO.isExist(name);
        if (exist) {
            request.setAttribute("msg","用户名已经被注册");
            return "register.jsp";
        }

        /*如果用户名没重复，则新建用户*/
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        System.out.println(user.getName());
        System.out.println(user.getPassword());
        /*向数据库中添加数据*/
        userDAO.add(user);

        /*注册成功，客户端跳转*/
        return "@registerSuccess.jsp";
    }

    public String login(HttpServletRequest request,HttpServletResponse response, Page page) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        User user = userDAO.get(name,password);
        if(null == user) {
            request.setAttribute("msg","账号或者密码错误");
            return "login.jsp";
        }
        request.getSession().setAttribute("user",user);
        return "@forehome";
    }

    public String logout(HttpServletRequest request,HttpServletResponse response, Page page) {
        request.getSession().removeAttribute("user");
        return "@forehome";
    }

    public String product(HttpServletRequest request,HttpServletResponse response,Page page) {
        int pid = Integer.parseInt(request.getParameter("pid"));
        Product p = productDAO.get(pid);

        /*image和imageDetail*/
        List<ProductImage> productSingleImages = productImageDAO.list(p,productImageDAO.type_single);
        List<ProductImage> productDetailImages = productImageDAO.list(p, ProductImageDAO.type_detail);
        p.setProductSingleImages(productSingleImages);
        p.setProductDetailImages(productDetailImages);

        List<Review> reviews = reviewDAO.list(pid);
        List<PropertyValue> pvs = propertyValueDAO.list(pid);

        /*设置产品的销量和评价数量*/
        productDAO.setSaleAndReviewNumber(p);

        /*向product.jsp中传递数据*/
        request.setAttribute("p",p);
        request.setAttribute("reviews",reviews);
        request.setAttribute("pvs",pvs);

        return "product.jsp";
    }

    public String checkLogin(HttpServletRequest request,HttpServletResponse response,Page page) {
        User user = (User) request.getSession().getAttribute("user");
        if(null == user) {
            return "%fail";
        }
        /*js中有监听，如果返回success，则重新加载页面*/
        return "%success";
    }

    public String loginAjax(HttpServletRequest request,HttpServletResponse response, Page page) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        User user = userDAO.get(name,password);
        if(null == user) {
            return "%fail";
        }

        request.getSession().setAttribute("user",user);
        return "%success";
    }

    public String category(HttpServletRequest request,HttpServletResponse response,Page page) {
        int cid = Integer.parseInt(request.getParameter("cid"));

        Category c = new CategoryDAO().get(cid);
        new ProductDAO().fill(c);
        new ProductDAO().setSaleAndReviewNumber(c.getProducts());

        String sort = request.getParameter("sort");
        if(null != sort) {
            switch(sort){
                case "review":
                    Collections.sort(c.getProducts(),new ProductReviewComparator());
                    break;
                case "date" :
                    Collections.sort(c.getProducts(),new ProductDateComparator());
                    break;

                case "saleCount" :
                    Collections.sort(c.getProducts(),new ProductSaleCountComparator());
                    break;

                case "price":
                    Collections.sort(c.getProducts(),new ProductPriceComparator());
                    break;

                case "all":
                    Collections.sort(c.getProducts(),new ProductAllComparator());
                    break;
            }
        }
        request.setAttribute("c",c);
        return "category.jsp";
    }

    public String search(HttpServletRequest request,HttpServletResponse response,Page page) {
        /*  1. 获取参数keyword
            2. 根据keyword进行模糊查询，获取满足条件的前20个产品
            3. 为这些产品设置销量和评价数量
            4. 把产品结合设置在request的"ps"属性上
            5. 服务端跳转到 searchResult.jsp 页面*/
        String keyword = request.getParameter("keyword");
        List<Product> ps = new ProductDAO().search(keyword,0,20);
        productDAO.setSaleAndReviewNumber(ps);
        request.setAttribute("ps",ps);
        return "searchResult.jsp";
    }

    public String buyone(HttpServletRequest request,HttpServletResponse response, Page page) {
        int pid = Integer.parseInt(request.getParameter("pid"));
        /*通过js传递出来*/
        int num = Integer.parseInt(request.getParameter("num"));
        Product p = productDAO.get(pid);
        int oiid = 0;

        /*分两种情况，购物车中有这个订单项，购物车中没有这个订单项*/
        User user = (User) request.getSession().getAttribute("user");
        boolean found = false;
        List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
        for(OrderItem oi : ois) {
            if(oi.getProduct().getId() == p.getId()) {
                oi.setNumber(oi.getNumber()+num);
                orderItemDAO.update(oi);
                found = true;
                oiid = oi.getId();
                break;
            }
        }

        /*如果没有相同产品订单项*/
        if(!found) {
            OrderItem oi = new OrderItem();
            oi.setNumber(num);
            oi.setProduct(p);
            oi.setUser(user);
            orderItemDAO.add(oi);
            oiid = oi.getId();
        }
        return "@forebuy?oiid="+oiid;
    }

    public String buy(HttpServletRequest request,HttpServletResponse response,Page page) {
        /*这个方法，既要接收直接购买的orderitem，也要接收通过购物车来购买的*/
        String[] oiids = request.getParameterValues("oiid");
        List<OrderItem> ois = new ArrayList<>();
        float total = 0;

        for(String strid:oiids) {
            int oiid = Integer.parseInt(strid);
            OrderItem oi = orderItemDAO.get(oiid);
            /*用打折后的价格计算*/
            total += oi.getProduct().getPromotePrice()*oi.getNumber();
            ois.add(oi);
        }

        /*request.getsession()这里需要getsession,因为ois是放在session中的，而不是放在request中的*/
        request.getSession().setAttribute("ois",ois);
        request.setAttribute("total",total);
        return "buy.jsp";
    }

    public String addCart(HttpServletRequest request,HttpServletResponse response,Page page) {
        int pid = Integer.parseInt(request.getParameter("pid"));
        Product p = productDAO.get(pid);

        int num = Integer.parseInt(request.getParameter("num"));
        User user = (User) request.getSession().getAttribute("user");
        boolean found = false;

        /*如果购物车中有相同产品则一同计算*/
        List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
        for(OrderItem oi : ois) {
            if(oi.getProduct().getId() == p.getId()) {
                    oi.setNumber(oi.getNumber()+num);
                orderItemDAO.update(oi);
                found = true;
                break;
            }
        }

        /*如果没有相同商品*/
        if(!found) {
            OrderItem oi = new OrderItem();
            oi.setNumber(num);
            oi.setProduct(p);
            oi.setUser(user);
            orderItemDAO.add(oi);
        }
        return "%success";
    }

    public String cart(HttpServletRequest request,HttpServletResponse response,Page page) {
        User user = (User) request.getSession().getAttribute("user");
        List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
        request.setAttribute("ois",ois);
        return "cart.jsp";
    }

    /*购物车调整订单项的数量*/
    public String changeOrderItem(HttpServletRequest request,HttpServletResponse response,Page page) {
        User user = (User) request.getSession().getAttribute("user");
        if(null == user) {
            return "%fail";
        }
        int pid = Integer.parseInt(request.getParameter("pid"));
        int number = Integer.parseInt(request.getParameter("number"));
        List<OrderItem> ois = orderItemDAO.listByUser(user.getId());
        for(OrderItem oi : ois) {
            if(oi.getProduct().getId() == pid) {
                oi.setNumber(number);
                orderItemDAO.update(oi);
                break;
            }
        }
        return "%success";
    }

    /*删除订单项*/
    public String deleteOrderItem(HttpServletRequest request,HttpServletResponse response,Page page) {
        User user = (User) request.getSession().getAttribute("user");
        if(null == user) {
            return "%fail";
        }
        int oiid = Integer.parseInt(request.getParameter("oiid"));
        orderItemDAO.delete(oiid);
        return "%success";
    }

    /*创建订单*/
    /*public String createOrder(HttpServletRequest request,HttpServletResponse response,Page page) {
        System.out.println("----------");
        User user = (User) request.getSession().getAttribute("user");
        List<OrderItem> ois = (List<OrderItem>) request.getSession().getAttribute("ois");
        *//*这里在业务上需要验证的是 是否存在订单项，即便是登陆了也会有订单项不存在*//*
        if(ois.isEmpty()) {
            return "@login.jsp";
        }
        *//*获取用户输入信息*//*
        String address = request.getParameter("address");
        String post = request.getParameter("post");
        String receiver = request.getParameter("receiver");
        String mobile = request.getParameter("mobile");
        String userMessage = request.getParameter("userMessage");

        Order order = new Order();
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())
                + RandomUtils.nextInt(10000);

        order.setOrderCode(orderCode);
        order.setAddress(address);
        order.setPost(post);
        order.setReceiver(receiver);
        order.setMobile(mobile);
        order.setUserMessage(userMessage);
        order.setCreateDate(new Date());
        order.setUser(user);
        order.setStatus(OrderDAO.waitPay);

        orderDAO.add(order);
        float total = 0;
        for(OrderItem oi: ois) {
            oi.setOrder(order);
            orderItemDAO.update(oi);
            total += oi.getProduct().getPromotePrice()*oi.getNumber();
        }
        return "@forealipay?oid="+order.getId()+"&total="+total;
    }*/
    public String createOrder(HttpServletRequest request, HttpServletResponse response, Page page){
        User user =(User) request.getSession().getAttribute("user");
        List<OrderItem> ois= (List<OrderItem>) request.getSession().getAttribute("ois");
        if(ois.isEmpty()) {
            return "@login.jsp";
        }

        String address = request.getParameter("address");
        String post = request.getParameter("post");
        String receiver = request.getParameter("receiver");
        String mobile = request.getParameter("mobile");
        String userMessage = request.getParameter("userMessage");

        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) +RandomUtils.nextInt(10000);
        Order order = new Order();
        order.setOrderCode(orderCode);
        order.setAddress(address);
        order.setPost(post);
        order.setReceiver(receiver);
        order.setMobile(mobile);
        order.setUserMessage(userMessage);
        order.setCreateDate(new Date());
        order.setUser(user);
        order.setStatus(OrderDAO.waitPay);

        orderDAO.add(order);

        float total =0;
        for (OrderItem oi: ois) {
            oi.setOrder(order);
            orderItemDAO.update(oi);
            total+=oi.getProduct().getPromotePrice()*oi.getNumber();
        }

        return "@forealipay?oid="+order.getId() +"&total="+total;
    }

    public String alipay(HttpServletRequest request,HttpServletResponse response,Page page) {
        return "alipay.jsp";
    }

    public String payed(HttpServletRequest request,HttpServletResponse response,Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order order = orderDAO.get(oid);
        order.setStatus(OrderDAO.waitDelivery);
        order.setPayDate(new Date());
        /*这里为什么要使用new*/
        new OrderDAO().update(order);
        request.setAttribute("o",order);
        return "payed.jsp";
    }

    public String bought(HttpServletRequest request,HttpServletResponse response,Page page) {
        User user = (User) request.getSession().getAttribute("user");
        List<Order> os = orderDAO.list(user.getId(),OrderDAO.delete);

        orderItemDAO.fill(os);
        request.setAttribute("os",os);
        return "bought.jsp";
    }

    public String confirmPay(HttpServletRequest request,HttpServletResponse response,Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = orderDAO.get(oid);
        orderItemDAO.fill(o);
        request.setAttribute("o",o);
        return "confirmPay.jsp";
    }

    public String orderConfirmed(HttpServletRequest request,HttpServletResponse response, Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = orderDAO.get(oid);
        o.setStatus(OrderDAO.waitReview);
        /*确认订单时间*/
        o.setConfirmDate(new Date());
        orderDAO.update(o);
        return "orderConfirmed.jsp";
    }

    public String deleteOrder(HttpServletRequest request,HttpServletResponse response,Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = orderDAO.get(oid);
        /*订单信息很重要，所以只标记为删除即可*/
        o.setStatus(OrderDAO.delete);
        orderDAO.update(o);
        return "%success";
    }

    /*评价*/
    public String review(HttpServletRequest request, HttpServletResponse response,Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = orderDAO.get(oid);
        orderItemDAO.fill(o);
        /*当订单有多个订单项时候，默认取第一个订单项对应的产品*/
        Product p = o.getOrderItems().get(0).getProduct();
        List<Review> reviews = reviewDAO.list(p.getId());
        productDAO.setSaleAndReviewNumber(p);
        request.setAttribute("o",o);
        request.setAttribute("p",p);
        request.setAttribute("reviews",reviews);
        return "review.jsp";
    }

    /*提交评价*/
    public String doreview(HttpServletRequest request,HttpServletResponse response,Page page) {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order o = orderDAO.get(oid);
        o.setStatus(OrderDAO.finish);
        orderDAO.update(o);
        /*订单评价的时候，是不是应该循环将评价放在所有的产品中呢，这样好像只会默认评价第一个*/
        int pid = Integer.parseInt(request.getParameter("pid"));
        Product p = productDAO.get(pid);

        /*reviewPage页面name为content的输入框*/
        String content = request.getParameter("content");
        /*对输入内容进行转义，用户有可能在输入框输入非正常评价*/
        content = HtmlUtils.htmlEscape(content);

        User user = (User) request.getSession().getAttribute("user");
        Review review = new Review();
        review.setContent(content);
        review.setCreateDate(new Date());
        review.setUser(user);
        review.setProduct(p);
        reviewDAO.add(review);

        return "@forereview?oid"+oid+"&showonly=true";
    }
}