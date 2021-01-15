package com.example.persimmon_tree_proj;

public class popup_plan {
    private String coloricon ;
    private String plan_name ;
    private String nick_name ;
    private String user_name;
    private String plan_id;

    public void setIcon(String icon) {
        coloricon = icon ;
    }
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

    public String getIcon() {
        return this.coloricon ;
    }
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