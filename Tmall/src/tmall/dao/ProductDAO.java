package tmall.dao;

import java.sql.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tmall.bean.Category;
import tmall.bean.Product;
import tmall.bean.User;

import tmall.util.DBUtil;
import tmall.util.DateUtil;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: acer zjl
 * \* Date: 2018/4/16 20:43
 * \* Description:
 * \
 */
public class ProductDAO {

    /* 增加 */
    public void add(Product bean) {
        String sql = "insert into product set values(null,?,?,?,?,?,?,? )";

        try (Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bean.getName());
            ps.setString(2, bean.getSubTitle());
            ps.setFloat(3, bean.getOrignalPrice());
            ps.setFloat(4, bean.getPromotePrice());
            ps.setInt(5, bean.getStock());
            ps.setInt(6, bean.getCategory().getId());
            ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
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

    /* 2. 删除 */
    public void delete(int id) {
        String sql = "delete from product where id = ?";

        try (Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* 6. 查询所有 */
    public List<Product> list() {
        return list(0, Short.MAX_VALUE);
    }

    /* 5. 分页查询 */
    public List<Product> list(int start, int count) {
        List<Product> beans = new ArrayList<Product>();
        String        sql   = "select * from Product limit ?,? ";

        try (Connection c = DBUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1, start);
            ps.setInt(2, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product bean         = new Product();
                int     id           = rs.getInt(1);
                int     cid          = rs.getInt("cid");
                String  name         = rs.getString("name");
                String  subTitle     = rs.getString("subTitle");
                float   orignalPrice = rs.getFloat("orignalPrice");
                float   promotePrice = rs.getFloat("promotePrice");
                int     stock        = rs.getInt("stock");
                Date    createDate   = DateUtil.t2d(rs.getTimestamp("createDate"));

                bean.setName(name);
                bean.setSubTitle(subTitle);
                bean.setOrignalPrice(orignalPrice);
                bean.setPromotePrice(promotePrice);
                bean.setStock(stock);
                bean.setCreateDate(createDate);
                bean.setId(id);

                Category category = new CategoryDAO().get(cid);

                bean.setCategory(category);
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return beans;
    }

    /* 3. 修改 */
    public void update(Product bean) {
        String sql = "update product set name = ?,subTitle = ?,orignalPrice =?,promotePrice = ?,stock = ?"
                     + "cid = ?,createDate = ? where id = ?";

        try (Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bean.getName());
            ps.setString(2, bean.getSubTitle());
            ps.setFloat(3, bean.getOrignalPrice());
            ps.setFloat(4, bean.getPromotePrice());
            ps.setInt(5, bean.getStock());
            ps.setInt(6, bean.getCategory().getId());
            ps.setTimestamp(7, DateUtil.d2t(bean.getCreateDate()));
            ps.setInt(8, bean.getId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setFirstProductImage(Product firstProductImage) {
       // this.firstProductImage = firstProductImage;
    }

    /* 4. 根据id获取 */
    public Product get(int id) {
        Product bean = null;
        String  sql  = "select * from product where id = ?";

        try (Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                bean = new Product();

                String name         = rs.getString("name");
                String subTitle     = rs.getString("subTitle");
                float  orignalPrice = rs.getFloat("orignalPrice");
                float  promotePrice = rs.getFloat("promotePrice");
                int    stock        = rs.getInt("stock");
                int    cid          = rs.getInt("cid");
                Date   createDate   = DateUtil.t2d(rs.getTimestamp("createDate"));

                bean.setName(name);
                bean.setSubTitle(subTitle);
                bean.setOrignalPrice(orignalPrice);
                bean.setPromotePrice(promotePrice);
                bean.setStock(stock);

                Category category = new CategoryDAO().get(cid);

                bean.setCategory(category);
                bean.setCreateDate(createDate);
                bean.setId(id);
                setFirstProductImage(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public int getTotal(int cid) {
        int total = 0;

        try (Connection c = DBUtil.getConnection();
            Statement s = c.createStatement();) {
            String    sql = "select count(*) from Product where cid = " + cid;
            ResultSet rs  = s.executeQuery(sql);

            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

     /**
      * @Description:
      * @Author:         zjl
      * @CreateDate:     2018/4/22 21:28
     */
    public void test() {

    }
}



