package tmall.dao;

import tmall.bean.Review;
import tmall.bean.User;
import tmall.util.DBUtil;
import tmall.util.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: acer zjl
 * \* Date: 2018/4/21 19:05
 * \* Description:
 * \
 */
public class ReviewDAO {

    /*增加*/
    public void add(Review bean){
        String sql = "insert into review values(null,?,?,?,?)";
        try(Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,bean.getContent());
            ps.setInt(2,bean.getUser().getId());
            ps.setInt(3,bean.getProduct().getId());
            /*通过DateUtil的转换方法，将util类的实际转换成数据库的时间格式*/
            ps.setTimestamp(4, DateUtil.d2t(new Date()));

            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*2. 删除*/

    public void delete(int id) {
        try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {
            String sql = "delete from review where id = " + id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*3. 修改*/

    public void update(Review bean) {
        String sql = "update review set content = ?, uid = ?, pid = ?, createDate=? where id = ?";
        try (Connection conn = DBUtil.getConnection();PreparedStatement ps = conn.prepareStatement(sql) ) {
            ps.setString(1,bean.getContent());
            ps.setInt(2,bean.getUser().getId());
            ps.setInt(3,bean.getProduct().getId());
            ps.setTimestamp(4,DateUtil.d2t(bean.getCreateDate()));
            ps.setInt(5,bean.getId());

            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*4. 根据id获取*/

    public Review get(int id) {
        Review bean = null;
        try (Connection conn = DBUtil.getConnection();Statement s = conn.createStatement()) {
            String sql = "select * from review where id =" + id;
            ResultSet rs = s.executeQuery(sql);

            while (rs.next()) {
                bean = new Review();

                String content = rs.getString("content");
                int pid = rs.getInt("pid");
                int uid = rs.getInt("uid");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

                bean.setId(id);
                bean.setContent(content);
                bean.setProduct(new ProductDAO().get(pid));
                bean.setUser(new UserDAO().get(uid));
                bean.setCreateDate(createDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }


    /*5. 根据产品id来分页查询*/

    public List<Review> list(int pid, int start, int count) {
        List<Review> beans = new ArrayList<Review>();
        try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {
            String sql = "select * from review  where pid =" + pid +"order by id desc limit " + start +","+count;

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                Review bean = new Review();

                int id = rs.getInt("id");
                String content = rs.getString("content");
                int uid = rs.getInt("uid");
                Date createDate = DateUtil.t2d(rs.getTimestamp("createDate"));

                bean.setId(id);
                bean.setContent(content);
                bean.setProduct(new ProductDAO().get(pid));
                bean.setUser(new UserDAO().get(uid));
                bean.setCreateDate(createDate);

                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return beans;
    }

    /*6. 查询所有*/

    public List<Review> list(int pid) {
        return list(pid,0,Short.MAX_VALUE);
    }


    /*7. 获取当前产品的评价总数*/

    public int getCount(int pid) {
        int total = 0;
        String sql = "select count(*) from review where pid = ?";
        try (Connection conn = DBUtil.getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,pid);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    /*8.判断用户是否当前产品评论重复提交*/
    public boolean isExist(String content,int pid,int uid) {
        String sql = "select * from review where content = ? , pid = ? and uid = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,content);
            ps.setInt(2,pid);
            ps.setInt(3,uid);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
