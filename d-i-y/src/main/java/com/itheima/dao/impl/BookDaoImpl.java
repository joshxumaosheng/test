package com.itheima.dao.impl;

import com.itheima.dao.BookDao;
import com.itheima.pojo.Book;
import com.mysql.jdbc.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl implements BookDao {
    public List<Book> findAll()  {
        //获取数据库连接
        Connection connection=null;
        //创建预编译对象
        PreparedStatement preparedStatement=null;
        //结果集
        ResultSet resultSet=null;
        //创建一个集合来存放结果集
        List<Book> list=new ArrayList<Book>();
        //加载数据库驱动
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/solr","root","root");
                String sql="select * from book";
            preparedStatement= (PreparedStatement) connection.prepareStatement(sql);
            resultSet=preparedStatement.executeQuery();
            //处理结果集
           while (resultSet.next()){
             Book book=new Book();
             book.setId(  resultSet.getInt("id"));
             book.setName(  resultSet.getString("name"));
             book.setPrice(  resultSet.getFloat("price"));
             book.setPic(  resultSet.getString("pic"));
             book.setDesc(  resultSet.getString("desc"));
             list.add(book);

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
