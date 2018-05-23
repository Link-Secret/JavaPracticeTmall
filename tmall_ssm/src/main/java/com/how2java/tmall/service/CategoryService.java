package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.util.Page;

import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* Author: acer zjl
 * \* Date: 2018/5/22 14:47
 * \* Description:
 * \
 */
public interface CategoryService {
     List<Category> list(Page page);
     int total();
     void add(Category category);
}
