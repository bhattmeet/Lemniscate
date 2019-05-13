package com.example.asus.lemniscate;

public class Bkinfo_model {

    public String title,author,productsize,binding,printstyle,coverfile,coverpage;

    public String getCoverfile() {
        return coverfile;
    }

    public void setCoverfile(String coverfile) {
        this.coverfile = coverfile;
    }

    public String getCoverpage() {
        return coverpage;
    }

    public void setCoverpage(String coverpage) {
        this.coverpage = coverpage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getProductsize() {
        return productsize;
    }

    public void setProductsize(String productsize) {
        this.productsize = productsize;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public String getPrintstyle() {
        return printstyle;
    }

    public void setPrintstyle(String printstyle) {
        this.printstyle = printstyle;
    }
}
