<<<<<<< HEAD
=======
/*package com.example.persimmon_tree_proj;
>>>>>>> 73bc7b9e5705b78cb740202bb69c465aa78e370f

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
        mDatabase.child("groups").child(fcode).setValue(group); //데이터 덮어쓰기
    }
}
<<<<<<< HEAD

=======
//package com.example.persimmon_tree_proj;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.security.acl.Group;
//
//public class GroupFamily {
//
//    public Integer fcode;
//
//    public GroupFamily(){
//
//    }
//
//    public GroupFamily(Integer fcode){
//        this.fcode = fcode;
//    }
//        private void writeGroupFamily(Integer fcode){
//        GroupFamily groupFamily = new GroupFamily(fcode);
//        mDatabase.child("groups").child(fcode).setValue(group); //데이터 덮어쓰기
//    }
//}
// */
>>>>>>> 6b8387e1965efeb57180d23cfe757fefe89280b0
=======
 */
>>>>>>> 73bc7b9e5705b78cb740202bb69c465aa78e370f

