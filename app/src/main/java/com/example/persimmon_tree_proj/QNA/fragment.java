//package com.example.persimmon_tree_proj.QNA;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.fragment.app.Fragment;
//
//import com.example.Juang_juang.R;
//
//public class fragment extends Fragment {
//
//    private View view;
//    private TextView fg_ans;
//    private static final String ARG_NO = "ARG_NO";
//
//    public AnsFragment() {
//    }
//
//    public static AnsFragment getInstance(int no) {
//        AnsFragment fragment = new AnsFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_NO, no);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment, container, false);
//        fg_ans = view.findViewById(R.id.fgtv_answer); //findViewById가 바로 안불러와져서 inflater하고 해야함
//        return view;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        int no = getArguments().getInt(ARG_NO, 0);
//        String text = "" + no + "번째 프래그먼트";
//        Log.d("MyFragment", "onCreate " + text);
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        TextView textView = (TextView) view.findViewById(R.id.text);
//        int no = getArguments().getInt(ARG_NO, 0);
//        String text = "" + no + "번째 프래그먼트";
//        Log.d("MyFragment", "onViewCreated " + text);
//        textView.setText(text);
//    }
//}
