package tmall.dao;

import tmall.bean.Category;
import tmall.util.DBUtil;

import javax.naming.Name;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: acer zjl
 * \* Date: 2018/4/13 17:02
 * \* Description:
 * \
 */
public class CategoryDAO {

    /*根据id增加分类*/
    /*try-resources-with写法，不用关闭connection，jdk1.7新语法*/
    public void add(Category bean){
        String sql = "insert into category values(null,?)";
        try (Connection conn = DBUtil.getConnection();PreparedStatement ps = conn.prepareStatement(sql)){
            /**/
            ps.setString(1,bean.getName());
            ps.execute();
            /*得到自动生成的主键*/
            ResultSet rs = ps.getGeneratedKeys();
            /*将得到的id注入到对象中*/
            if(rs.next()) {
                int id = rs.getInt(1);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*删除*/
    public void delete(int id){
       /* try (Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()){
            String sql = "delete fromm category where id ="+id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
       String sql = "delete from category where id = ?";
       try (Connection conn = DBUtil.getConnection();PreparedStatement ps = conn.prepareStatement(sql)){
           ps.setInt(1,id);
           ps.execute();
       } catch (SQLException e) {
           e.printStackTrace();
       }
    }
    /*更新*/
    public void update(Category bean){
        String sql = "update category set name = ? where id = ?";
        try (Connection conn = DBUtil.getConnection();PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1,bean.getName());
            ps.setInt(2,bean.getId());

            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*查询单个*/
    public Category get(int id) {
        Category bean = null;
        String sql = "select id,name from category where id = ?";
        try (Connection conn = DBUtil.getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                bean = new Category();
                String name = rs.getString(2);
                bean.setId(id);
                bean.setName(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }
    /*分页查询*/
    public List<Category> list(int start,int count){
        List<Category> beans = new ArrayList<Category>();
        /*从后往前展示*/
        String sql = "select * from category order by id desc limit ?,?";
        try(Connection conn = DBUtil.getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,start);
            ps.setInt(2,count);
            /*得到SQL语句执行的查询结果*/
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                /*新建一个category存储数据*/
                Category bean = new Category();
                bean.setId(rs.getInt(1));
                bean.setName(rs.getString(2));
                /*添加到List中去*/
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }
    /*查询所有*/
    public List<Category> list() {
        return list(0,Short.MAX_VALUE);
    }
    /*得到总数*/
    public int getTotal() {
        int total = 0;
        try(Connection conn = DBUtil.getConnection();Statement s = conn.createStatement()) {
            String sql = "select count(*) from category";

            ResultSet rs = s.executeQuery(sql);
            /*如果返回数据，则赋值给total*/
            while(rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

}


