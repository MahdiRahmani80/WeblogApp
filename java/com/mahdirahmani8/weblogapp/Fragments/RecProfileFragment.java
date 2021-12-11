package com.mahdirahmani8.weblogapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mahdirahmani8.API.APIClient;
import com.mahdirahmani8.API.APIInterface;
import com.mahdirahmani8.Model.HomeAllBlog;
import com.mahdirahmani8.weblogapp.Adopter.ProfileShowMyBlog;
import com.mahdirahmani8.weblogapp.DataBase.DataBase;
import com.mahdirahmani8.weblogapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecProfileFragment extends Fragment {

    public RecProfileFragment(int miniORblog){  // 0 for blog

    }

    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_rec,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        recyclerView =  view.findViewById(R.id.Rec_Profile);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getMyPosts();
    }

    private void getMyPosts() {
        APIInterface request = APIClient.getAPIListClient(APIClient.url).create(APIInterface.class);
        DataBase db = new DataBase(getContext());
        Call<HomeAllBlog> call =  request.getMyBlogs(APIInterface.auth,db.getId());
        call.enqueue(new Callback<HomeAllBlog>() {
            @Override
            public void onResponse(Call<HomeAllBlog> call, Response<HomeAllBlog> response) {

                if (getActivity() != null &&response.code() == 200){
                    ProfileShowMyBlog adapter = new ProfileShowMyBlog(getContext(),response.body());
                    recyclerView.setAdapter(adapter);

                } else {
                    // some error bottom shit
                }
            }

            @Override
            public void onFailure(Call<HomeAllBlog> call, Throwable t) {

            }
        });

    }
}
