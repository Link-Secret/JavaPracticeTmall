package tmall.dao;

import tmall.bean.Order;
import tmall.bean.OrderItem;
import tmall.bean.Product;
import tmall.bean.User;
import tmall.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* Author: acer zjl
 * \* Date: 2018/4/22 15:59
 * \* Description:
 * \
 */
public class OrderItemDAO {

    /**
    *增加
    **/
    public void add(OrderItem bean){
        String sql = "insert into orderItem values(null,?,?,?,?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,bean.getProduct().getId());

            /*订单项生成的时候是没有订单的*/
            if(null == bean.getOrder()) {
                ps.setInt(2,-1);
            }else {
                ps.setInt(2,bean.getOrder().getId());
            }

            ps.setInt(3,bean.getUser().getId());
            ps.setInt(4,bean.getNumber());

            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*2. 删除*/

    public void delete(int id) {
        try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {
            String sql = "delete from orderitem where id = " + id;

            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*3. 修改*/

    public void update(OrderItem bean) {
        String sql = "update orderitem set pid = ?, oid = ?, uid = ? ,number = ? where id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,bean.getProduct().getId());
            ps.setInt(2,bean.getOrder().getId());
            ps.setInt(3,bean.getUser().getId());
            ps.setInt(4,bean.getNumber());
            ps.setInt(5,bean.getId());

            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*4. 根据id获取*/

    public OrderItem get(int id) {
        OrderItem bean = null;
        try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {
            String sql = "select * from orderItem where id = ?";

            ResultSet rs  = s.executeQuery(sql);
            while (rs.next()) {
                bean = new OrderItem();

                int pid = rs.getInt("pid");
                int oid = rs.getInt("oid");
                int uid = rs.getInt("uid");
                int number = rs.getInt("number");

                bean.setId(id);
                bean.setProduct(new ProductDAO().get(pid));
                bean.setOrder(new OrderDAO().get(oid));
                bean.setUser(new UserDAO().get(uid));
                bean.setNumber(number);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }


    /*5. 分页查询用户未生成订单的订单项*/

    public List<OrderItem> listByUser(int uid, int start, int count) {
        List<OrderItem> beans = new ArrayList<OrderItem>();
        String sql = "select * from orderitem where uid = ? and oid = -1 order by id DESC limit ?,?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,uid);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem bean = new OrderItem();

                int id = rs.getInt("id");
                int pid = rs.getInt("pid");
                int oid = rs.getInt("oid");
                int number = rs.getInt("number");

                Product product = new ProductDAO().get(pid);
                if(-1 != oid) {
                    Order order = new OrderDAO().get(oid);
                    bean.setOrder(order);
                }
                User user = new UserDAO().get(uid);

                bean.setId(id);
                bean.setProduct(product);

                bean.setUser(user);
                bean.setNumber(number);

                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

    /*6. 查询当前用户所有未生成订单的订单项*/

    public List<OrderItem> listByUser(int uid) {
        return listByUser(uid,0,Short.MAX_VALUE);
    }


    /*7. 获取所有订单项总数*/

    public int getTotal() {
        int total = 0;
        try (Connection c = DBUtil.getConnection(); Statement s = c.createStatement();) {

            String sql = "select count(*) from OrderItem";

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return total;
    }

    /*8.根据订单id来获取所有的订单项*/

    public List<OrderItem> listByOrder(int oid, int start, int count) {
        List<OrderItem> beans = new ArrayList<OrderItem>();
        String sql = "select * from orderitem where oid = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,oid);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem bean = new OrderItem();

                int id = rs.getInt("id");
                int pid = rs.getInt("pid");
                int uid = rs.getInt("uid");
                int number = rs.getInt("number");

                bean.setId(id);
                bean.setProduct(new ProductDAO().get(pid));
                bean.setOrder(new OrderDAO().get(oid));
                bean.setUser(new UserDAO().get(uid));
                bean.setNumber(number);

                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

    public List<OrderItem> listByOrder(int uid) {
        return listByUser(uid,0,Short.MAX_VALUE);
    }

    /**
    * 10.填充订单
    * */
    public void fill(Order o) {
        /*得到当前订单的所有订单项*/
       List<OrderItem> ois = listByOrder(o.getId());
       float total = 0;
       for(OrderItem oi : ois) {
           /*total 总价格*/
           total += oi.getProduct().getPromotePrice()*oi.getNumber();
       }
       /*订单*/
       o.setToatal(total);
       o.setOrderItems(ois);
    }

    /*
    *11. 填充多个订单
    * */

    public void fill(List<Order> os) {
        for(Order o : os) {
            List<OrderItem> ois = listByOrder(o.getId());
            float total = 0;
            int totalNumber = 0;
            for(OrderItem oi : ois) {
                total += oi.getNumber() * oi.getProduct().getPromotePrice();
                totalNumber += oi.getNumber();
            }
            o.setOrderItems(ois);
            o.setToatal(total);
            o.setTotalNumber(totalNumber);
        }
     }

    /**
     *12得到某个产品的所有订单项
     */
    public List<OrderItem> listByProduct(int pid) {
        return listByProduct(pid,0,Short.MAX_VALUE);
    }

    public List<OrderItem> listByProduct(int pid,int start, int count) {
        List<OrderItem> beans = new ArrayList<OrderItem>();
        String sql = "select * from orderitem where pid = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,pid);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem bean = new OrderItem();

                int id = rs.getInt("id");
                int oid = rs.getInt("oid");
                int uid = rs.getInt("uid");
                int number = rs.getInt("number");

                Order order = new OrderDAO().get(oid);
                Product product = new ProductDAO().get(pid);
                User user = new UserDAO().get(uid);

                bean.setId(id);
                bean.setNumber(number);
                bean.setUser(user);
                bean.setProduct(product);
                bean.setOrder(order);

                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }


    /**
     * 14得到某个产品的销量
     * */
    public int getSaleCount(int pid) {
        int total = 0;
        try (Connection conn = DBUtil.getConnection();Statement s = conn.createStatement()) {
            String sql = "select sum(number) from orderItem where pid = " + pid;

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
}
