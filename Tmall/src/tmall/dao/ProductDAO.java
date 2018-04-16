package tmall.dao;

import tmall.bean.Category;
import tmall.bean.Product;
import tmall.bean.User;
import tmall.util.DBUtil;
import tmall.util.DateUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: acer zjl
 * \* Date: 2018/4/16 20:43
 * \* Description:
 * \
 */
public class ProductDAO {


    /*增加*/
    public void add(Product bean){
        String sql = "insert into product set values(null,?,?,?,?,?,?,? )";
        try(Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,bean.getName());
            ps.setString(2,bean.getSubTitle());
            ps.setFloat(3,bean.getOriginPrice());
            ps.setFloat(4,bean.getPromotePrice());
            ps.setInt(5,bean.getStock());
            ps.setInt(6,bean.getCategory().getId());
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


    /*2. 删除*/

    public void delete(int id) {

    }


    /*3. 修改*/

    public void update(Product bean) {

    }


    /*4. 根据id获取*/

    public Product get(int id) {
        Product bean = null;
        String sql = "select * from product where id = ?";
        try (Connection conn = DBUtil.getConnection();PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bean = new Product();

                String name = rs.getString("name");
                String subTitle = rs.getString("subTitle");
                float orignalPrice = rs.getFloat("orignalPrice");
                float promotePrice = rs.getFloat("promotePrice");
                int stock = rs.getInt("stock");
                int cid = rs.getInt("cid");
                Date createDate = DateUtil.t2d( rs.getTimestamp("createDate"));


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


    /*5. 分页查询*/

    public List<Product> list(int start, int count) {
        return null;
    }

    /*6. 查询所有*/

    public List<Product> list() {
        return null;
    }


    /*7. 获取总数*/

    public int getTotal() {
        return 0;
    }

}
