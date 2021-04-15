package com.example.persimmon_tree_proj.Family;

import android.graphics.drawable.Drawable;

public class ListViewItem {
    private Drawable backgrounddrawble;
    private Drawable circledrawble;
    private String namestr;

    public void setbackground(Drawable background) {
        backgrounddrawble = background ;
    }
    public void setCircle(Drawable circle) {
        circledrawble = circle ;
    }
    public void setName(String name) {
        namestr = name ;
    }

    public Drawable getbackground() {
        return this.backgrounddrawble ;
    }
    public Drawable getCircle() {
        return this.circledrawble ;
    }
    public String getName() {
        return this.namestr;
    }
}
