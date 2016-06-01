package com.shuan.myland.list;

public class PropertyList {
    String id, pro_type, plot_id, pro_deatils, price, area, a_type, man_img;

    public PropertyList() {
    }

    public PropertyList(String id, String pro_type, String plot_id, String pro_deatils, String price, String area, String a_type, String man_img) {
        this.id = id;
        this.pro_type = pro_type;
        this.plot_id = plot_id;
        this.pro_deatils = pro_deatils;
        this.price = price;
        this.area = area;
        this.a_type = a_type;
        this.man_img = man_img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPro_type() {
        return pro_type;
    }

    public void setPro_type(String pro_type) {
        this.pro_type = pro_type;
    }

    public String getPlot_id() {
        return plot_id;
    }

    public void setPlot_id(String plot_id) {
        this.plot_id = plot_id;
    }

    public String getPro_deatils() {
        return pro_deatils;
    }

    public void setPro_deatils(String pro_deatils) {
        this.pro_deatils = pro_deatils;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getA_type() {
        return a_type;
    }

    public void setA_type(String a_type) {
        this.a_type = a_type;
    }

    public String getMan_img() {
        return man_img;
    }

    public void setMan_img(String man_img) {
        this.man_img = man_img;
    }
}
