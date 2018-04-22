package tmall.dao;

import tmall.bean.PropertyValue;
import tmall.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
        String sql = "insert into propertyvalue set(null,?,?,?");
        try (Connection conn = DBUtil.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1,bean.);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*2. 删除*/

    public void delete(int id) {

    }


    /*3. 修改*/

    public void update(P bean) {

    }


    /*4. 根据id获取*/

    public User get(int id) {
        return null;
    }


    /*5. 分页查询*/

    public List<User> list(int start, int count) {
        return null;
    }

    /*6. 查询所有*/

    public List<User> list() {
        return null;
    }


    /*7. 获取总数*/

    public int getTotal() {
        return 0;
    }
}
