package com.shuan.myland.list;


public class PropertyItem {

    String id,pro_id,pro_img,pro_price,pos_date,pro_det,pro_type,pro_loc,pro_area,pro_status;
    String pro_sub_img;
    String pro_name,pro_atype;

    public PropertyItem() {
    }

    public PropertyItem(String pro_sub_img) {
        this.pro_sub_img = pro_sub_img;
    }


    public PropertyItem(String id,String pro_id,String pro_img, String pro_price,String pos_date, String pro_det, String pro_type, String pro_loc,String pro_area, String pro_status) {
        this.id=id;
        this.pro_id=pro_id;
        this.pro_img = pro_img;
        this.pro_price = pro_price;
        this.pos_date = pos_date;
        this.pro_det = pro_det;
        this.pro_type = pro_type;
        this.pro_loc=pro_loc;
        this.pro_area = pro_area;
        this.pro_status = pro_status;
    }

    public PropertyItem(String id,String pro_id,String pro_name,String pro_price,String pro_img,String pro_loc){
        this.id=id;
        this.pro_id=pro_id;
        this.pro_name=pro_name;
        this.pro_price=pro_price;
        this.pro_loc=pro_loc;
        this.pro_img=pro_img;

    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getPro_atype() {
        return pro_atype;
    }

    public void setPro_atype(String pro_atype) {
        this.pro_atype = pro_atype;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPro_loc() {
        return pro_loc;
    }

    public void setPro_loc(String pro_loc) {
        this.pro_loc = pro_loc;
    }

    public String getPro_img() {
        return pro_img;
    }

    public void setPro_img(String pro_img) {
        this.pro_img = pro_img;
    }

    public String getPro_price() {
        return pro_price;
    }

    public void setPro_price(String pro_price) {
        this.pro_price = pro_price;
    }

    public String getPos_date() {
        return pos_date;
    }

    public void setPos_date(String pos_date) {
        this.pos_date = pos_date;
    }

    public String getPro_det() {
        return pro_det;
    }

    public void setPro_det(String pro_det) {
        this.pro_det = pro_det;
    }

    public String getPro_type() {
        return pro_type;
    }

    public void setPro_type(String pro_type) {
        this.pro_type = pro_type;
    }

    public String getPro_area() {
        return pro_area;
    }

    public void setPro_area(String pro_area) {
        this.pro_area = pro_area;
    }

    public String getPro_status() {
        return pro_status;
    }

    public void setPro_status(String pro_status) {
        this.pro_status = pro_status;
    }

    public String getPro_sub_img() {
        return pro_sub_img;
    }

    public void setPro_sub_img(String pro_sub_img) {
        this.pro_sub_img = pro_sub_img;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }
}
