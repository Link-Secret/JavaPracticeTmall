package tmall.dao;

import com.sun.xml.internal.bind.v2.model.core.ID;
import tmall.bean.User;
import tmall.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: acer zjl
 * \* Date: 2018/4/13 19:12
 * \* Description:
 * \
 */
public class UserDAO {

    /*增加*/
    public void add(User bean){
        /*id,name,password*/
        String sql = "insert user values(null,?,?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,bean.getName());
            ps.setString(2,bean.getPassword());
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                int id = rs.getInt(1);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


/*2. 删除*/

    public void delete(int id) {
        try(Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {
            String sql = "delete from user where id =" + id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


/*3. 修改*/

    public void update(User bean) {
        String sql = "update user set name = ? ,password = ? where id = ?";
        try (Connection conn = DBUtil.getConnection();PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1,bean.getName());
            ps.setString(2,bean.getPassword());
            ps.setInt(3,bean.getId());
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


/*4. 根据id获取*/

    public User get(int id) {
        User bean = null;
        try(Connection conn = DBUtil.getConnection();Statement s = conn.createStatement()){
            String sql = "select * from user where id = " + id;
            ResultSet rs = s.executeQuery(sql);
            while(rs.next()) {
                bean = new User();
                bean.setId(id);
                bean.setName(rs.getString(2));
                bean.setPassword(rs.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }


/*5. 分页查询*/

    public List<User> list(int start, int count) {
        List<User> beans = new ArrayList<User>();
        String sql = "select * from user order by id desc limit ?,?";
        try(Connection conn = DBUtil.getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,start);
            ps.setInt(2,count);
            ResultSet rs = ps.executeQuery();

            /*根据查询结果映射对象*/
            while(rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");

                /*创建对象*/
                User bean = new User();
                bean.setId(id);
                bean.setName(name);
                bean.setPassword(password);

                /*添加到list中*/
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

/*6. 查询所有*/

    public List<User> list() {
        return list(0,Short.MAX_VALUE);
    }


/*7. 获取总数*/

    public int getTotal() {
        int total = 0;
        try (Connection conn = DBUtil.getConnection();Statement s = conn.createStatement()){
            String sql = "select count(*) from user";
            ResultSet rs = s.executeQuery(sql);
            while(rs.next()) {
                /*total = rs.getInt("count");*/
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    /*注册的时候，需要判断某个用户是否已经存在*/
    /*根据用户名获取对象*/
    public User get(String name) {
        User bean = null;

        String sql = "select * from user where name = ?";
        try (Connection conn = DBUtil.getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,name);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                bean = new User();
                bean.setId(rs.getInt("id"));
                bean.setName(name);
                bean.setPassword(rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }

    /*以boolean形式返回某个用户名是否已经存在*/
    public boolean isExist(String name) {
        User user = get(name);
        return user != null;
    }

    /*根据账号和密码获取对象，这才是合理的判断
    账号密码是否正确的方式，而不是一下把所有的用户信息查出来，在内存中进行比较*/
    public User get(String name,String password) {
        User bean = null;

        String sql = "select * from user where name = ? and password = ?";
        try(Connection conn = DBUtil.getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,name);
            ps.setString(2,password);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                bean.setName(rs.getString("name"));
                bean.setPassword(rs.getString("password"));
                bean.setId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }
}









