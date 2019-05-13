package com.example.asus.lemniscate;

public class Postmodel {

    private String bookname,postid,postimage,caption,username,loves,hashtags,time,profileimage,appreciations,followers,following;


    public Postmodel(String postid, String postimage, String bookname, String caption, String username, String loves, String hashtags, String time, String profileimage,String appreciations,String followers,String following){
        this.postid = postid;
        this.postimage = postimage;
        this.caption = caption;
        this.username = username;
        this.loves = loves;
        this.hashtags = hashtags;
        this.time = time;
        this.profileimage = profileimage;
        this.appreciations = appreciations;
        this.followers = followers;
        this.following = following;
        this.bookname = bookname;
    }

    public Postmodel(){

    }

    public String getAppreciations() {
        return appreciations;
    }

    public void setAppreciations(String appreciations) {
        this.appreciations = appreciations;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getLoves() {
        return loves;
    }

    public void setLoves(String loves) {
        this.loves = loves;
    }

    public String getHashtags() {
        return hashtags;
    }

    public void setHashtags(String hashtags) {
        this.hashtags = hashtags;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }
}
