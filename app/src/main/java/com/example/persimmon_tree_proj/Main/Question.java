package com.example.persimmon_tree_proj.Main;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Question { //질문을 추가하거나 조회할 데이터 클래스

    public String content;

    public Question() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Question(String content) {
        this.content = content;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("content",content);
        return result;
    }
}
