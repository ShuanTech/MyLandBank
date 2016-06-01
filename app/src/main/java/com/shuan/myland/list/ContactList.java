package com.shuan.myland.list;

/**
 * Created by team-leader on 4/16/2016.
 */
public class ContactList {
    int img;
    String head, addr, land, ph, fax;

    public ContactList() {
    }

    public ContactList(int img, String head, String addr, String ph, String land, String fax) {
        this.img = img;
        this.head = head;
        this.addr = addr;
        this.land = land;
        this.ph = ph;
        this.fax = fax;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }
}
