//package com.example.persimmon_tree_proj.QNA;
//
//import android.app.FragmentManager;
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentStatePagerAdapter;
//
//public class FragmentAdapter extends FragmentStatePagerAdapter {
//    public FragmentAdapter(FragmentManager fm) {
//        super(fm);
//    }
//    @Override
//    public Fragment getItem(int position) {
//        Fragment f = new AnsFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt("position", position);
//        f.setArguments(bundle);
//        return f;
//    }
//    @Override
//    public int getCount() { return answer; }
//}
