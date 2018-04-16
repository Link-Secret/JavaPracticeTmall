package tmall.bean;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: acer zjl
 * \* Date: 2018/4/12 19:35
 * \* Description:
 * \
 */
public class User {
    private int id;
    private String name;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAnonymousName(String name) {
        /*name为空*/
        if(null == name)
            return null;
        if(name.length() == 1){
            return "*";
        }
        if(name.length() == 2) {
            return name.substring(0,1)+"*";
        }
        /*昵称大于2个的*/
        else{
            char[] ch = name.toCharArray();
            for(int i=1;i<ch.length-1;i++) {
               ch[i] = '*';
            }
            return new String(ch);
        }
    }
}
