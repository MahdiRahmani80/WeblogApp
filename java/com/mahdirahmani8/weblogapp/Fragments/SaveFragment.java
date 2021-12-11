package com.mahdirahmani8.weblogapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.mahdirahmani8.weblogapp.R;

public class SaveFragment extends Fragment {

    View view;
//    TabLayout tabLayout;
    ViewPager viewPager;
    TabLayoutProfileFragment adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable  Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.save_fragment,container,false);
        cast();
        setFragment();

        return view;
    }

    private void cast() {
//        tabLayout = view.findViewById(R.id.tabLayout_save);
        viewPager = view.findViewById(R.id.view_pager_save);
    }

    private void setFragment() {
        adapter = new TabLayoutProfileFragment(getChildFragmentManager());

        RecSaveFragment blog = new RecSaveFragment(0);
//        RecSaveFragment miniblog = new RecSaveFragment(1);

        adapter.addFragment(blog,"");
//        adapter.addFragment(miniblog,"MINI BLOGS");

        viewPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(viewPager);
    }
}