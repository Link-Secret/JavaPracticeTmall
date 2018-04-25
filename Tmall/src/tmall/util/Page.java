package tmall.util;

/**
 * \* Created with IntelliJ IDEA.
 * \* Author: acer zjl
 * \* Date: 2018/4/23 20:51
 * \* Description:
 * \
 */
public class Page {
    int start;
    int count;
    int total;
    String param;

    public Page(int start, int count) {
        this.start = start;
        this.count = count;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }


    /**
     * @Description:    其他需要使用到的方法
     * @Author.Time:    zjl 2018/4/24 11:41
    */
     public boolean isHasPrevious() {
         if (0 == start) {
             return false;
         }
         return true;
     }

     public boolean isHasNext() {
         if(start == getLast()) {
             return false;
         }
         return true;
     }

     /**
      * @Description:   得到最好一页的start
      * @Author.Time:    zjl 2018/4/24 11:47
     */
    public int getLast() {
        int last;
        /*最后一页刚好为count*/
        if(0 == total % count) {
            last = total - count;
        }else {
            last = total - total % count;
        }
        last = last > 0 ? last : 0;
        return last;
    }

    /**
     * @Description:    得到页数
     * @Author.Time:    zjl 2018/4/24 11:52
    */
    public int getTotalPage() {
        int totalPage = 0;
        if(0 == total % count) {
            totalPage = total % count;
        }else {
            totalPage = total / count + 1;
        }
        /*判断total=0的时候，totalpage为0，所以要设为1*/
        totalPage = totalPage == 0 ? 1 :totalPage;
        return totalPage;
    }



}
