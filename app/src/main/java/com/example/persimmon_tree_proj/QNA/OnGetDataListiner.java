package com.example.persimmon_tree_proj.QNA;

import com.google.firebase.database.DataSnapshot;

public interface OnGetDataListiner {
    void onSuccess(DataSnapshot dataSnapshot);

    //    void onCallback(String value);
    //void onSuccess(DataSnapshot dataSnapshot);
    void onStart();
    void onFailure();
}
