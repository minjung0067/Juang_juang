package com.example.persimmon_tree_proj;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserModel {
    //사용자의 정보가 담기는 model
    public String name; //사용자 이름
    public String uid; //현재 로그인한 사용자
    public String phone;
    public String birth;
    public UserModel(){

    }
    public UserModel(String name, String uid, String phone, String birth){
        this.name = name;
        this.uid = uid;
        this.phone = phone;
        this.birth = birth;
    }

    @Exclude
    public Map<String, Object> toMap() { //해당 child의 값을 입력할 HashMap을 선언
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("name", name);
        result.put("phone", phone);
        result.put("birth", birth);
        return result;
    }

}
