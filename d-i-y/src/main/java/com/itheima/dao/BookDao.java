package com.itheima.dao;

import com.itheima.pojo.Book;

import java.util.List;

public interface BookDao {
    /**
     * 定义方法查询数据
     */
    List<Book> findAll();
}
