package tmall.dao;

import tmall.bean.Product;
import tmall.bean.Property;
import tmall.bean.PropertyValue;
import tmall.bean.User;
import tmall.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: acer zjl
 * \* Date: 2018/4/20 10:40
 * \* Description:
 * \
 */
public class PropertyValueDAO {

    /*增加*/
    public void add(PropertyValue bean){
        String sql = "insert into propertyvalue set(null,?,?,?)";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1,bean.getProduct().getId());
            ps.setInt(2,bean.getProperty().getId());
            ps.setString(3,bean.getValue());
            ps.execute();

            /*得到主键*/
            ResultSet rs = ps.getGeneratedKeys();
            while(rs.next()) {
                int id = rs.getInt(1);
                bean.setId(id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*2. 删除*/

    public void delete(int id) {
        try (Connection conn = DBUtil.getConnection();Statement s = conn.createStatement()) {
            String sql = "delete from propertyvalue where id = " + id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*3. 修改*/

    public void update(PropertyValue bean) {
        String sql = "update property set pid = ?,ptid = ? ,value = ? where id = ?";
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,bean.getProduct().getId());
            ps.setInt(2,bean.getProperty().getId());
            ps.setString(3,bean.getValue());
            ps.setInt(4,bean.getId());
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*4. 根据id获取*/

    public PropertyValue get(int id) {
        PropertyValue bean = null;
        try (Connection conn = DBUtil.getConnection();Statement s = conn.createStatement()) {
            String sql = "select * from propertyvallue where id = " + id;
            ResultSet rs = s.executeQuery(sql);

            while (rs.next()) {
                int pid = rs.getInt("pid");
                int ptid = rs.getInt("ptid");
                String value = rs.getString("value");

                Product product = new ProductDAO().get(pid);
                Property property = new PropertyDAO().get(ptid);

                /*实例化property对象*/
                bean.setId(id);
                bean.setProduct(product);
                bean.setProperty(property);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }


    /*5. 查询所有属性值*/
    /*1. 这些方法都是典型的DAO方法，不过并没有在实际业务中被调用.
    2. 虽然没有在实际业务中被调用，但是依然在测试的过程中有可能使用到，比如我在对应页面功能未完善的时候，查看下有多少数据，就可以调用getTotal(), 而不是必须到mysql 客户端中去调用一个SQL语句。
    3. 更新的时候，肯定是根据自身的id去更新的啊，你看看update()方法
        String sql = "update PropertyValue set pid= ?, ptid=?, value=?  where id = ?";*/

    public List<PropertyValue> list( int start, int count) {
        List<PropertyValue> beans = new ArrayList<PropertyValue>();
        String sql = "select * from propertyvalue  order by id limit ?,?";
        try (Connection conn = DBUtil.getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,start);
            ps.setInt(2,count);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PropertyValue bean = new PropertyValue();

                int id = rs.getInt("id");
                int pid = rs.getInt("pid");
                int ptid = rs.getInt("ptid");
                String value = rs.getString("value");

                Product product = new ProductDAO().get(pid);
                Property property = new PropertyDAO().get(ptid);
                PropertyValue propertyValue = new PropertyValueDAO().get(id);

                bean.setId(id);
                bean.setProduct(product);
                bean.setProperty(property);
                bean.setValue(value);

                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

    /*6. 查询所有*/
    public  List<PropertyValue> list() {
        return list(0,Short.MAX_VALUE);
    }


    /*7. 获取列的总数*/

    public int getTotal() {
        int total = 0;
        String sql = "select count(*) from propertyvalue";
        try (Connection conn = DBUtil.getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    /*8.根据产品id和属性id获取propertyvalue对象*/
    public PropertyValue get(int ptid,int pid) {
        PropertyValue bean = null;
        try (Connection conn = DBUtil.getConnection();Statement s = conn.createStatement()) {
            String sql = "select * from propertyvalue where ptid = "+ptid+" and pid = " + pid;
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                bean = new PropertyValue();

                int id = rs.getInt("id");
                String value = rs.getString("value");

                bean.setId(id);
                bean.setProduct(new ProductDAO().get(pid));
                bean.setProperty(new PropertyDAO().get(ptid));
                bean.setValue(value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bean;
    }

    /*9.初始化某个产品对应的属性值*/
    public void init(Product p) {
        /*得到该分类的所有属性集合*/
       List<Property> pts = new PropertyDAO().list(p.getCategory().getId());

       /*遍历每个属性*/
       for(Property pt : pts ) {
           /*根据属性和产品，获取属性值*/
            PropertyValue pv =this.get(pt.getId(),p.getId());

            /*如果属性值不存在，就创建一个属性值对象*/
            if(null == pv) {
                pv = new PropertyValue();
                pv.setProperty(pt);
                pv.setProduct(p);
                /*此时初始化调用add方法，属性值需要手动添加*/
                this.add(pv);
            }
       }
    }

    /*10,查询当前产品的所有属性值，因为属性值不是很多，所以不用分类*/
    public List<PropertyValue> list(int pid) {
        List<PropertyValue> beans = new ArrayList<PropertyValue>();
        String sql = "select * from propertyvalue where pid = ?";
        try (Connection conn = DBUtil.getConnection();PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,pid);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                /*新建一个propertyvalue对象*/
                PropertyValue bean = new PropertyValue();

                /*得到查询的数据*/
                int id = rs.getInt("id");
                int ptid = rs.getInt("ptid");
                String value = rs.getString("value");

                /*propertyvalue对象*/
                bean.setId(id);
                bean.setProduct(new ProductDAO().get(pid));
                bean.setProperty(new PropertyDAO().get(ptid));
                bean.setValue(value);

                /*加入到集合中*/
                beans.add(bean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return beans;
    }

}
