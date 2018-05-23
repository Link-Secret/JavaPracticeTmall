package com.how2java.tmall.mapper;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.util.Page;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* Author: acer zjl
 * \* Date: 2018/5/22 14:41
 * \* Description:
 * \
 */
public interface CategoryMapper {
     public List<Category> list(Page page);
     public int total();
     public void add(Category category);
}
