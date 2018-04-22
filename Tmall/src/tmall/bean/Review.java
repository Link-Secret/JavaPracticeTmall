package tmall.bean;

import java.util.Date;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: acer zjl
 * \* Date: 2018/4/13 16:14
 * \* Description:
 * \
 */
public class Review {

    private int id;
    private User user;
    private Product product;
    private String content;
    private Date createDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
