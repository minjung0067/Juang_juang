package com.example.persimmon_tree_proj.QNA;

public class QNAListViewItem {

    private String contentStr;
    private String numStr;

    public void setNum(String num){
        numStr = num;
    }

    public void setContent(String content){
        contentStr = content;
    }

    public String getNum(){
        return this.numStr;
    }

    public String getContent(){
        return this.contentStr;
    }

}
