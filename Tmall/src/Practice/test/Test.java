package Practice.test;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: acer zjl
 * \* Date: 2018/4/13 15:04
 * \* Description:
 * \
 */
public class Test {

    public static void main(String[] args) {
        String name = "zjl";
        Test test = new Test();
        name = test.getAnonymousName(name);
        System.out.println(name);
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
            /*return String.valueOf(ch);*/
        }
    }
}
