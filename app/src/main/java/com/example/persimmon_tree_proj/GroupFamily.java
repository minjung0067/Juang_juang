<<<<<<< HEAD
=======
package com.example.persimmon_tree_proj;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.security.acl.Group;

public class GroupFamily {

    public Integer fcode;

    public GroupFamily(){

    }

    public GroupFamily(Integer fcode){
        this.fcode = fcode;
    }

    private void writeGroupFamily(Integer fcode){
        GroupFamily groupFamily = new GroupFamily(fcode);
        //mDatabase.child("groups").child(fcode).setValue(group); //데이터 덮어쓰기
    }
}



>>>>>>> d8c27509eca314ca7771a168ed570e7b7b1854e6
