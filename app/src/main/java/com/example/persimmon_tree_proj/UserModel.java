package com.example.persimmon_tree_proj;

public class UserModel {
    //사용자의 정보가 담기는 model
    public String name; //사용자 이름
    public String uid; //현재 사용자
    public String phone;
    public String birth;
    public UserModel(String name, String uid, String phone, String birth){
        this.name = name;
        this.uid = uid;
        this.phone = phone;
        this.birth = birth;
    }

}
