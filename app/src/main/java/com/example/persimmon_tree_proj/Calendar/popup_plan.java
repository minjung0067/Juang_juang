package com.example.persimmon_tree_proj.Calendar;

import android.graphics.drawable.Drawable;

public class popup_plan {
    private String coloricon ;
    private String plan_name ;
    private String nick_name ;
    private String user_gam;
    private String user_name;
    private String plan_id;
    private Drawable backgrounddrawable;


    public void setbackground(Drawable background) {
        backgrounddrawable = background ;
    }
    public void setIcon(String icon) {
        coloricon = icon ;
    }
    public void setUser_gam(String gam) {user_gam = gam ;}
    public void setNick_name(String nickname) {
        nick_name = nickname ;
    }
    public void setPlan_name(String planname) {
        plan_name = planname ;
    }
    public void setUser_name(String username) {
        user_name = username ;
    }
    public void setPlan_id(String planid) {
        plan_id = planid ;
    }

    public Drawable getbackground() {return this.backgrounddrawable; }
    public String getIcon() {
        return this.coloricon ;
    }
    public String getUser_gam() {return this.user_gam; }
    public String getNick_name() {
        return this.nick_name ;
    }
    public String getPlan_name() {
        return this.plan_name ;
    }
    public String getUser_name() {
        return this.user_name ;
    }
    public String getPlan_id( ) {
        return this.plan_id;
    }
}