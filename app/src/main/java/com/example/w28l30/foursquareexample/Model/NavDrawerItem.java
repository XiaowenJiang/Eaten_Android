package com.example.w28l30.foursquareexample.Model;

/**
 * Created by W28L30 on 15/11/23.
 */
public class NavDrawerItem {
    private boolean showNotify;
    private String title;
    private String count = "0";


    public NavDrawerItem() {

    }

    public NavDrawerItem(boolean showNotify, String title, String count) {
        this.showNotify = showNotify;
        this.title = title;
        this.count = count;
    }

    public String getCount(){
        return this.count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
