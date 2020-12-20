
package com.example.persimmon_tree_proj;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import java.security.acl.Group;
import java.util.HashMap;
import java.util.Map;

public class GroupFamily {

    public String fcode;
    public String str_code;

    public GroupFamily(){
        // Default constructor required for calls to DataSnapshot.getValue(GroupFamily.class)
    }

    public GroupFamily(String str_code){
        this.fcode = str_code;
    }

    public String getFcode(){return str_code;}
    public void setFcode(){this.fcode = str_code; }

    //나는 멥으로 묶을 필요가 없는데.. 일단 적었습!
    /*
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("fcode", str_code);
        return result;
    }
    */
}


