package com.example.asus.lemniscate;

public class CartModel {

    private String pid,bookname,gener,bookimage,time,date,quantity,price;

    public CartModel(){

    }

    public CartModel(String pid, String bookname,String gener,String bookimage, String time, String date, String quantity, String price) {
        this.pid = pid;
        this.bookname = bookname;
        this.bookimage = bookimage;
        this.gener = gener;
        this.time = time;
        this.date = date;
        this.quantity = quantity;
        this.price = price;
    }


    public String getBookimage() {
        return bookimage;
    }

    public void setBookimage(String bookimage) {
        this.bookimage = bookimage;
    }

    public String getGener() {
        return gener;
    }

    public void setGener(String gener) {
        this.gener = gener;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
