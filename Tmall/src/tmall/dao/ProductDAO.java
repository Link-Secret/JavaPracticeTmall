package tmall.dao;

import java.sql.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tmall.bean.Category;
import tmall.bean.Product;
import tmall.bean.ProductImage;
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

    /**
     * @Description:
     * @Author.Time:    zjl 2018/4/23 10:01
    */
    public void setFirstProductImage(Product p) {
        /*查询出所有类型为type_single的图片*/
        List<ProductImage> pis = new ProductImageDAO().list(p, ProductImageDAO.type_single);
        if(!pis.isEmpty()) {
            /*默认设置第一张类型为type_single的图片为第一张产品图片*/
            p.setFirstProductImage(pis.get(0));
        }
    }

    /**
     * @Description:    为产品设置销量
     * @Author.Time:    zjl 2018/4/23 10:11
    */
    public  void setSaleAndReviewNumber(Product p) {
        int saleCount = new OrderItemDAO().getSaleCount(p.getId());
        p.setSaleCount(saleCount);

        int reviewCount = new ReviewDAO().getCount(p.getId());
        p.setReviewCount(reviewCount);
    }

    /**
     * @Description:
     * @Author.Time:    zjl 2018/4/23 10:15
    */
    public void setSaleAndReviewNumber(List<Product> ps) {
        for(Product p : ps) {
            setSaleAndReviewNumber(p);
        }
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

    /**
     * @Description:
     * @Author:         zjl
     * @CreateDate:     2018/4/23 9:06
    */
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
     * @Description:    根据cid来分页
     * @Author:         zjl
     * @CreateDate:     2018/4/23 9:12
    */
    public List<Product> list(int cid) {
        return list(cid,0,Short.MAX_VALUE);
    }

    /**
     * @Description:
     * @Author:         zjl
     * @CreateDate:     2018/4/23 9:14
    */
    public List<Product> list(int cid, int start, int count) {
        List<Product> beans = new ArrayList<Product>();
        Category category = new CategoryDAO().get(cid);
        String sql = "select * from Product where cid = ? order by id desc limit ?,? ";

        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setInt(1, cid);
            ps.setInt(2, start);
            ps.setInt(3, count);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Product bean = new Product();
                int id = rs.getInt(1);
                String name = rs.getString("name");
                String subTitle = rs.getString("subTitle");
                float orignalPrice = rs.getFloat("orignalPrice");
                float promotePrice = rs.getFloat("promotePrice");
                int stock = rs.getInt("stock");
                Date createDate = DateUtil.t2d( rs.getTimestamp("createDate"));

                bean.setName(name);
                bean.setSubTitle(subTitle);
                bean.setOrignalPrice(orignalPrice);
                bean.setPromotePrice(promotePrice);
                bean.setStock(stock);
                bean.setCreateDate(createDate);
                bean.setId(id);
                bean.setCategory(category);
                setFirstProductImage(bean);
                beans.add(bean);
            }
        } catch (SQLException e) {

            e.printStackTrace();
        }
        return beans;
    }


    /**
     * @Description:    填充
     * @Author:         zjl
     * @CreateDate:     2018/4/23 9:22
    */
    public void fill(Category c) {
        List<Product> ps = this.list(c.getId());
        c.setProducts(ps);
    }

    /**
     * @Description:
     * @Author:         zjl
     * @CreateDate:     2018/4/23 9:25
    */
    public  void fill(List<Category> cs) {
        for(Category c: cs) {
            fill(c);
        }
    }

    /**
     * @Description:  一个分类分多行展示产品，这个方法就是填充这个分类，分行填充
     * @Author.Time:  zjl 2018/4/23 9:30
    */
    public  void fillByRow(List<Category> cs) {
        int productNumberEachRow = 8;
        for(Category c : cs) {
            List<Product> products = c.getProducts();
            /*这个分类的每行的集合*/
            List<List<Product>> productsByRow = new ArrayList<>();
            /*从0到这个分类所有的产品数，最后一行如果不足设置的默认产品数*/
            for (int i =0; i<products.size(); i += productNumberEachRow) {
                int size = i + productNumberEachRow;
                /*最后一行的判断*/
                size = size > products.size() ? products.size() : size;
                /*每行产品的集合*/
                List<Product> productsOfEachRow = products.subList(i,size);
                /*这个分类的每行的集合添加这行产品*/
                productsByRow.add(productsOfEachRow);
            }
            /*每个分类填充这个分类的行的集合*/
            c.setProductsByRow(productsByRow);
        }
    }


    /**
     * @Description:    根据关键字模糊查询
     * @Author.Time:    zjl 2018/4/23 10:16
    */
    public List<Product> search(String keyword,int start, int count) {
        List<Product> beans = new ArrayList<Product>();
        if(null == keyword || 0 == keyword.trim().length()) {
            return beans;
        }
        String sql = "select * from product where name like ? limit ?, ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1,"%" + keyword.trim() + "%");
            ps.setInt(2,start);
            ps.setInt(3,count);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Product bean = new Product();
                int id = rs.getInt(1);
                int cid = rs.getInt("cid");
                String name = rs.getString("name");
                String subTitle = rs.getString("subTitle");
                float orignalPrice = rs.getFloat("orignalPrice");
                float promotePrice = rs.getFloat("promotePrice");
                int stock = rs.getInt("stock");
                Date createDate = DateUtil.t2d( rs.getTimestamp("createDate"));

                bean.setName(name);
                bean.setSubTitle(subTitle);
                bean.setOrignalPrice(orignalPrice);
                bean.setPromotePrice(promotePrice);
                bean.setStock(stock);
                bean.setCreateDate(createDate);
                bean.setId(id);

                Category category = new CategoryDAO().get(cid);
                bean.setCategory(category);
                setFirstProductImage(bean);
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }
}



