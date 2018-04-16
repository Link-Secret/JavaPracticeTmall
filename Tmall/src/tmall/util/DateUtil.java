package tmall.util;

import java.sql.Timestamp;
import java.util.Date;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: acer zjl
 * \* Date: 2018/4/13 16:45
 * \* Description:
 * \
 */
public class DateUtil {

    /*java.util.Date类 -> java.sql.Timestamp 类的互相转换*/
    public static java.sql.Timestamp d2t(java.util.Date date) {
        if(null == date) {
            return null;
        }
        return new Timestamp(date.getTime());
    }

    /*java.sql.Timestamp -> java.util.Data*/
    public static java.util.Date t2d(java.sql.Timestamp timestamp) {
        if(null == timestamp) {
            return null;
        }
        return new java.util.Date(timestamp.getTime());
    }
}
