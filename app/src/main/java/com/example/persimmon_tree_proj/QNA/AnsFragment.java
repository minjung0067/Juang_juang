//package com.example.persimmon_tree_proj.QNA;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.example.Juang_juang.R;
//
//public class AnsFragment extends Fragment {
//    private View ans_view;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        ans_view = inflater.inflate(R.layout.frag_answer, container, false); //view를 inflate라는 개념을 써서 frag_answer가져옴
////        View rootView = inflater.inflate(R.layout.fragement_layout, container, false);
////        Bundle extra = getArguments();
//        int a = extra.getInt("position");
//        TextView tv = (TextView)ans_view.findViewById(R.id.tv);
//        tv.setText(a + 1 + "번째 Fragment");
//        return ans_view; //return 하면 보여짐
//    }
//}
