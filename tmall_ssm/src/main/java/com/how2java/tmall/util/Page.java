package com.how2java.tmall.util;

/**
 * \* Created with IntelliJ IDEA.
 * \* Author: acer zjl
 * \* Date: 2018/5/23 14:09
 * \* Description:
 * \
 */
public class Page {
    /*分页类的几个属性*/

    private int start;
    private int count;
    private int total;
    private String param;

    //默认每页数据
    private static final int defaultCount =  5;

    //构造方法
    public Page() {
        count = defaultCount;
    }

    public Page(int start,int count) {
        this();
        this.count = count;
        this.start = start;
    }

    //分页的方法

    //最后一页的start数
    public int getLast() {
        int last;
        if(total % count == 0) {
            last = total - count;
        }else {
            last = total - total % count;
        }
        /*last可能小于0*/
        return last < 0 ? 0 : last;
    }

    //是否有上一页
    public boolean isHasPreviouse() {
        if (start == 0) {
            return false;
        }
        return true;
    }

    //是否有下一页
    public boolean isHasNext() {
        if(start == getLast()) {
            return false;
        }
        return true;
    }

    //页数
    public int getTotalPage() {
        int totalPage;
        if(total % count == 0) {
            totalPage = total / count;
        }else {
            totalPage = total / count + 1;
        }
        if(0 == totalPage) {
            totalPage = 1;
        }
        return totalPage;
    }

    //toString
    @Override
    public String toString() {
        return "Page [start=" + start + ", count=" + count + ", total=" + total + ", getStart()=" + getStart()
                + ", getCount()=" + getCount() + ", isHasPreviouse()=" + isHasPreviouse() + ", isHasNext()="
                + isHasNext() + ", getTotalPage()=" + getTotalPage() + ", getLast()=" + getLast() + "]";
    }
    //getter setter
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

}
