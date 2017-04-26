package com.surine.tustbox.Bean;

import org.litepal.crud.DataSupport;

/**
 * Created by surine on 2017/4/10.
 */

public class Book_Info extends DataSupport {
    private String book_name;   //书名
    private String author;   //作者
    private String dead_line;   //截至
    private String status;   //状态
    private String money;    //扣钱
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }


    public String getDead_line() {
        return dead_line;
    }

    public void setDead_line(String dead_line) {
        this.dead_line = dead_line;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
