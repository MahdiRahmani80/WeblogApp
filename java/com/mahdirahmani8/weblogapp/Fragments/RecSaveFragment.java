package com.mahdirahmani8.weblogapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mahdirahmani8.Model.AllMiniBlogs;
import com.mahdirahmani8.Model.Blog;
import com.mahdirahmani8.Model.HomeAllBlog;
import com.mahdirahmani8.weblogapp.Adopter.GetBlog_Adopter;
import com.mahdirahmani8.weblogapp.Adopter.MiniBlogAdopter;
import com.mahdirahmani8.weblogapp.DataBase.DataBase;
import com.mahdirahmani8.weblogapp.R;

import java.util.List;

public class RecSaveFragment extends Fragment {

    private int miniOrBlog;
    public RecSaveFragment(int miniOrBlog) {
        this.miniOrBlog = miniOrBlog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_rec, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.Rec_Profile);
        TextView tv =view.findViewById(R.id.tv_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DataBase db = new DataBase(getContext());

        if (miniOrBlog == 0 ){
            List<Blog> blogList =  db.getSavedBlogs(0);
            HomeAllBlog hab = new HomeAllBlog();
            hab.setResults(blogList);
            GetBlog_Adopter adopter = new GetBlog_Adopter(getContext(),hab);
            recyclerView.setAdapter(adopter);
            if (blogList.size() ==0){
                tv.setText("you not saved blog's");
            }
        }else{
            AllMiniBlogs miniBlogs = new AllMiniBlogs();
            miniBlogs.setResults(db.getSavedMiniBlogs(1));

            MiniBlogAdopter adapter = new MiniBlogAdopter (miniBlogs,getContext());
            recyclerView.setAdapter(adapter);
            if (db.getSavedMiniBlogs(1).size() ==0){
                tv.setText("you not saved mini blog's");
            }
        }
    }
}
