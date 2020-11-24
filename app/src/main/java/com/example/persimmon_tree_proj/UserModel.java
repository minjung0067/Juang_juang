package com.example.persimmon_tree_proj;

public class UserModel {
    //사용자의 정보가 담기는 model
    public String name; //사용자 이름
    public String uid; //현재 로그인한 사용자
    public String phone;
    public String birth;
    public Integer fcode;

    public UserModel() {

    }
    public UserModel(String name, String uid, String phone, String birth){
        this.name = name;
        this.uid = uid;
        this.phone = phone;
        this.birth = birth;
        this.fcode = fcode;
    }


    public String getUserName() {
        return name;
    }

    public void setUserName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public Integer getFamilyCode(){return fcode;}

    public Integer setFamilyCode(){return fcode;}
    @Override
    public String toString() {
        return "User{" +
                "userName='" + name + '\'' +
                ", uid ='" + uid + '\'' +
                ", phone='" + phone + '\'' +
                ", birth='" + birth + '\'' +
                ", family code=" + fcode + '\''+
                '}';
    }
}

