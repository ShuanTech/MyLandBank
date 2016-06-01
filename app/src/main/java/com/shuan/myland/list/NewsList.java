package com.shuan.myland.list;


public class NewsList {
    String id,head,img,date,desc;

    public NewsList() {
    }

    public NewsList(String id, String head, String img, String date) {
        this.id = id;
        this.head = head;
        this.img = img;
        this.date = date;
    }

    public NewsList(String id, String head, String img, String date, String desc) {
        this.id = id;
        this.head = head;
        this.img = img;
        this.date = date;
        this.desc = desc;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

