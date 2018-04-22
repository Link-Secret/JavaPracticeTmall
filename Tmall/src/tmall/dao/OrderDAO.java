package tmall.dao;

import com.sun.xml.internal.bind.v2.TODO;
import tmall.bean.Order;
import tmall.util.DBUtil;
import tmall.util.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: acer zjl
 * \* Date: 2018/4/21 20:44
 * \* Description:
 * \
 */
public class OrderDAO {

    public static final String waitPay = "waitPay";
    public static final String waitDelivery = "waitDelivery";
    public static final String waitCofirm = "waitConfirm";
    public static final String waitReview = "waitReview";
    public static final String finish = "finish";
    /*delete 逻辑删除*/
    public static final String delete = "delete";

    /**
    * 增加订单
    **/
    public void add(Order bean) {
        String sql = "insert into order_ values(null,?,?,?,?,?,?,?,?,?,?,?,?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bean.getOrderCode());
            ps.setString(2, bean.getAddress());
            ps.setString(3, bean.getPost());
            ps.setString(4, bean.getReceiver());
            ps.setString(5, bean.getPhone());
            ps.setString(6, bean.getUserMessage());

            ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
            ps.setTimestamp(8, DateUtil.d2t(bean.getPayDate()));
            ps.setTimestamp(9, DateUtil.d2t(bean.getDeliveryDate()));
            ps.setTimestamp(10, DateUtil.d2t(bean.getConfirmDate()));

            ps.setInt(11, bean.getUser().getId());
            ps.setString(12, bean.getStatus());

            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*2. 删除*/

    public void delete(int id) {
        try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {
            String sql = "delete from order_ where id = " + id;

            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*3. 修改*/

    public void update(Order bean) {
        String sql = "update order_ set address= ?, post=?, receiver=?,mobile=?,userMessage=? " +
                ",createDate = ? , payDate =? , deliveryDate =?, confirmDate = ? , orderCode =?, uid=?, status=? where id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bean.getOrderCode());
            ps.setString(2, bean.getAddress());
            ps.setString(3, bean.getPost());
            ps.setString(4, bean.getReceiver());
            ps.setString(5, bean.getPhone());
            ps.setString(6, bean.getUserMessage());

            ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
            ps.setTimestamp(8, DateUtil.d2t(bean.getPayDate()));
            ps.setTimestamp(9, DateUtil.d2t(bean.getDeliveryDate()));
            ps.setTimestamp(10, DateUtil.d2t(bean.getConfirmDate()));

            ps.setInt(11, bean.getUser().getId());
            ps.setString(12, bean.getStatus());

            ps.setInt(13, bean.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
    *4. 根据id获取
    */
    public Order get(int id) {
        Order bean = null;
        String sql = "select * from order_ where id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bean = new Order();

                /*获取数据查询的结果*/
                String orderCode = rs.getString("orderCode");
                String post = rs.getString("post");
                String receiver = rs.getString("receiver");
                String mobile = rs.getString("mobile");
                String userMessage = rs.getString("userMessage");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                Date payDate = DateUtil.t2d(rs.getTimestamp("payDate"));
                Date deliveryDate = DateUtil.t2d(rs.getTimestamp("deliveryDate"));
                Date confirmDate = DateUtil.t2d(rs.getTimestamp("confirmDate"));
                int uid = rs.getInt("uid");
                String status = rs.getString("status");

                /*填充bean*/
                bean.setId(id);
                bean.setOrderCode(orderCode);
                bean.setPost(post);
                bean.setReceiver(receiver);
                bean.setPhone(mobile);
                bean.setUserMessage(userMessage);
                bean.setCreateDate(createDate);
                bean.setPayDate(payDate);
                bean.setDeliveryDate(deliveryDate);
                bean.setConfirmDate(confirmDate);
                bean.setUser(new UserDAO().get(uid));
                bean.setStatus(status);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }


    /*5. 分页查询所有不属于某个状态的订单，比如只查询没有被删除的订单，订单是不会真的删除的。只是逻辑删除*/

    public List<Order> list(int uid, String excludedStatus, int start, int count) {
        List<Order> beans = new ArrayList<Order>();
        String sql = "select * from order_ where uid = ? and status != ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, uid);
            ps.setString(2, excludedStatus);

            /*根据返回结果建立对象*/
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order bean = new Order();

                /*获取数据查询的结果*/
                int id = rs.getInt("id");
                String orderCode = rs.getString("orderCode");
                String post = rs.getString("post");
                String receiver = rs.getString("receiver");
                String mobile = rs.getString("mobile");
                String userMessage = rs.getString("userMessage");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));
                Date payDate = DateUtil.t2d(rs.getTimestamp("payDate"));
                Date deliveryDate = DateUtil.t2d(rs.getTimestamp("deliveryDate"));
                Date confirmDate = DateUtil.t2d(rs.getTimestamp("confirmDate"));
                String status = rs.getString("status");

                /*填充bean*/
                bean.setId(id);
                bean.setOrderCode(orderCode);
                bean.setPost(post);
                bean.setReceiver(receiver);
                bean.setPhone(mobile);
                bean.setUserMessage(userMessage);
                bean.setCreateDate(createDate);
                bean.setPayDate(payDate);
                bean.setDeliveryDate(deliveryDate);
                bean.setConfirmDate(confirmDate);
                bean.setUser(new UserDAO().get(uid));
                bean.setStatus(status);

                /*添加到集合中*/
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*6. 查询所有*/

    public List<Order> list(int uid, String excludedStatus) {
        return list(uid, excludedStatus, 0, Short.MAX_VALUE);
    }


    /*7. 获取订单总数，因为是订单管理，后面可以自己修改下，根据UID来查询对应的订单*/

    public int getTotal() {
        int total = 0;
        /*没有？的时候，别使用Preparedstatement*/
        try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {
            String sql = "select count(*) from order_ ";
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
