package com.itheima.dao.impl;

import com.itheima.dao.BookDao;
import com.itheima.pojo.Book;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import org.apache.lucene.util.fst.Util;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl implements BookDao {
    public List<Book> queryBookList() throws Exception{
        //数据库连接
       Connection connection=null;
        //预编译statement
        PreparedStatement preparedStatement=null;
        //结果集
        ResultSet resultSet=null;
        //图书列表
        List<Book> list=new ArrayList<Book>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //连接数据库
           connection= (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/solr","root","root");
           //sql语句
            String sql="select * from book";
            //创建prepareStatement
            preparedStatement= (PreparedStatement) connection.prepareStatement(sql);
            //获取结果集
            resultSet = preparedStatement.executeQuery();
            //结果集解析
            while (resultSet.next()){
                Book book=new Book();
                book.setId(resultSet.getInt("id"));
                book.setName(resultSet.getString("name"));
                book.setPrice(resultSet.getFloat("price"));
                book.setPic(resultSet.getString("pic"));
                book.setDesc(resultSet.getString("desc"));
                list.add(book);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
}
