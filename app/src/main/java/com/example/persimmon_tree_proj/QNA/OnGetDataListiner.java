
package com.example.persimmon_tree_proj.QNA;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public interface OnGetDataListiner {
    void onSuccess();
    void onStart();
    void onFailure();
}
