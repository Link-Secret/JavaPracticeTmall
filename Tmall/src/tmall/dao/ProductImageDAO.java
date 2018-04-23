package tmall.dao;

import tmall.bean.Product;
import tmall.bean.ProductImage;
import tmall.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: acer zjl
 * \* Date: 2018/4/16 20:04
 * \* Description:
 * \
 */
public class ProductImageDAO {

    /*静态属性 type_single  type_detail*/
    public static final String type_single = "type_single";
    public static final String type_detail = "type_detail";

    /*增加productImage*/
    public void add(ProductImage bean){
        String sql = "insert productImage set values(null,?,?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){
            /*填充占位符*/
            ps.setInt(1,bean.getProduct().getId());
            ps.setString(2,bean.getType());
            ps.execute();
            /*得到主键*/
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) {
                int id = rs.getInt(1);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*2. 删除ProductImage*/

    public void delete(int id) {
        try(Connection conn = DBUtil.getConnection(); Statement s = conn.createStatement()) {
            String sql = "delete from productimage where id = " + id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*3. 修改ProductImage*/
    /*功能没有实现，后面可以考虑实现*/
    public void update(ProductImage bean) {
        String sql = "update productimage set pid =?,type = ? where id = ?";
        try(Connection conn = DBUtil.getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,bean.getProduct().getId());
            ps.setString(2,bean.getType());
            ps.setInt(3,bean.getId());
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*4. 根据id获取*/

    public ProductImage get(int id) {
        ProductImage bean = null;
        try(Connection conn = DBUtil.getConnection();Statement s = conn.createStatement()) {
            String sql = "select * from productimage where id =" + id;

            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                int pid = rs.getInt("pid");
                String type = rs.getString("type");
                Product product = new ProductDAO().get(pid);
                bean.setProduct(product);
                bean.setType(type);
                bean.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /*5. 根据产品分页查询,且分两种，一种type_single 一种type_detail*/

    public List<ProductImage> list(Product p, String type, int start, int count) {
        List<ProductImage> beans = new ArrayList<ProductImage>();
        String sql = "select * from productimage where pid = ? and type = ? order by id limit ? , ?";
        try (Connection conn = DBUtil.getConnection();PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1,p.getId());
            ps.setString(2,type);
            ps.setInt(3,start);
            ps.setInt(4,count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProductImage bean = new ProductImage();
                int id = rs.getInt("id");
                bean.setId(id);
                bean.setType(type);
                bean.setProduct(p);

                beans.add(bean);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

    /*6. 查询所有*/

    public List<ProductImage> list(Product p,String type) {
        return list(p,type,0,Short.MAX_VALUE);
    }


    /*7. 获取总数*/

    public int getTotal() {
        int total = 0;
        try(Connection conn = DBUtil.getConnection();Statement s = conn.createStatement()) {
            String sql = "select count(*) from productimage ";
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
