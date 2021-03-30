package com.example.persimmon_tree_proj.QNA;

public class UserModel {
    //사용자의 정보가 담기는 model
    public String name; //사용자 이름
    public String uid; //현재 로그인한 사용자
    public String birth;
    public String fcode;
    public String introduce;

    public UserModel(String uid){
        this.uid = uid;
    }


    public String getUserName() {
        return name;
    }

    public void setUserName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getFamilyCode(){return fcode;}

    public void setFamilyCode(){this.fcode = fcode; }

    public String getIntroduce(){return introduce;}

    public void setIntroduce(){this.introduce = introduce; }
    @Override
    public String toString() {
        return "User{" +
                "userName='" + name + '\'' +
                ", uid ='" + uid + '\'' +
                ", birth='" + birth + '\'' +
                ", family code='" + fcode + '\''+
                ", introduce my self ='" + introduce +'\''+
                '}';
    }
}