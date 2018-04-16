package tmall.dao;

import tmall.bean.Category;
import tmall.bean.Property;
import tmall.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: acer zjl
 * \* Date: 2018/4/15 21:27
 * \* Description:
 * \
 */
public class PropertyDAO {


    /*增加*/
    public void add(Property bean){
        String sql = "insert into property values(null,?,?)";
        try(Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            /*注入到SQL语句中*/
            ps.setInt(1,bean.getCategory().getId());
            ps.setString(2,bean.getName());

            /*执行ps*/
            ps.execute();

            /*得到主键id*/
            ResultSet rs = ps.getGeneratedKeys();
            while (rs.next()) {
                int id = rs.getInt("id");
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*2. 删除*/

    public void delete(int id) {
        try(Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {
            String sql = "delete from property where id =" + id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*3. 修改*/

    public void update(Property bean) {
        String sql = "update property set cid = ? ,name = ? where id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){
            /*填充？占位符中的数据*/
            ps.setInt(1,bean.getCategory().getId());
            ps.setString(2,bean.getName());
            ps.setInt(3,bean.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*4. 根据id获取*/

    public Property get(int id) {
        Property bean = null;
        try (Connection conn = DBUtil.getConnection();Statement s = conn.createStatement()){
            String sql = "select * from property where id = " + id;
            /*返回结果*/
            ResultSet rs = s.executeQuery(sql);
            /*如果rs中有值*/
            while(rs.next()) {
                /*通过cid来获得Category*/
                int cid = rs.getInt("cid");
                Category category = new CategoryDAO().get(cid);
                String name = rs.getString("name");

                bean.setId(id);
                bean.setCategory(category);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }


    /*5. 根据分类来分页查询*/

    public List<Property> list(int cid,int start, int count) {
        List<Property> beans = new ArrayList<Property>();
        String sql = "select * from property where cid = ? order by id desc limit ?,?";
        try (Connection conn = DBUtil.getConnection();PreparedStatement ps = conn.prepareStatement(sql)){
            /*填充占位符*/
            ps.setInt(1,cid);
            ps.setInt(2,start);
            ps.setInt(3,count);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Property bean = new Property();
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Category category = new CategoryDAO().get(cid);
                /*属性填充*/
                bean.setId(id);
                bean.setName(name);
                bean.setCategory(category);
                /*添加到集合*/
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return beans;
    }

    /*6. 查询所属分类所有属性*/

    public List<Property> list(int cid) {
        return list(cid,0,Short.MAX_VALUE);
    }


    /*7. 获取当前分类属性总数*/

    public int getTotal(int cid) {
        int total = 0;
        try (Connection conn = DBUtil.getConnection();Statement s = conn.createStatement()) {
            String sql = "select count(*) from property where cid = ?" + cid;
            ResultSet rs =s.executeQuery(sql);
            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

}
