package com.shuan.myland.list;

/**
 * Created by team-leader on 3/16/2016.
 */
public class MenuList {
    int id;
    String mnuName;

    public MenuList() {
    }

    public MenuList(int id, String mnuName) {
        this.id = id;
        this.mnuName = mnuName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMnuName() {
        return mnuName;
    }

    public void setMnuName(String mnuName) {
        this.mnuName = mnuName;
    }
}
