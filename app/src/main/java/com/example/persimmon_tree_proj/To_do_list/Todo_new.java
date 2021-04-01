package com.example.persimmon_tree_proj.To_do_list;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Todo_new {

    public String uid;
    public String writer;
    public String title;
    public String contents;
    public String style;
    public String date;

    public Map<String, Boolean> stars = new HashMap<>();

    public Todo_new() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Todo_new(String uid, String writer, String title, String contents, String style, String date) {
        this.uid = uid;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.style = style;
        this.date = date;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("writer", writer);
        result.put("title", title);
        result.put("contents", contents);
        result.put("style", style);
        result.put("date",date);
        return result;
    }
}