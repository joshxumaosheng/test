package com.itheima.pojo;

import java.io.Serializable;

/**
 * 创建pojo对象，用来接收从数据库查询过来的数据
 */
public class Book implements Serializable {
    // 图书ID
    private Integer id;
    // 图书名称
    private String name;
    // 图书价格
    private Float price;
    // 图书图片
    private String pic;
    // 图书描述
    private String desc;

    public Book() {
    }

    public Book(Integer id, String name, Float price, String pic, String desc) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.pic = pic;
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", pic='" + pic + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
