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
      List<Category> list();
      void add(Category category);
      void delete(int id);
      Category get(int id);
      void update(Category category);
}
