package com.mahdirahmani8.weblogapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.mahdirahmani8.API.APIClient;
import com.mahdirahmani8.API.APIInterface;
import com.mahdirahmani8.Model.User;
import com.mahdirahmani8.weblogapp.Adopter.GetBlog_Adopter;
import com.mahdirahmani8.weblogapp.DataBase.DataBase;
import com.mahdirahmani8.weblogapp.R;
import com.mahdirahmani8.weblogapp.Splash;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    View view;
    ImageView user_img;
    TextView sign_out,tv_username,tv_bio;
    String email,name,bio;
//    TabLayout tabLayout;
    ViewPager viewPager;
    TabLayoutProfileFragment adapter;

//    TODO : set Adopter  & set beautiful txt for create & set Retrofit

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable  Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.profile_fragment,container,false);
        cast();
        signOut();
        ImgPro();
        setFragment();

        return view;
    }

    private void setFragment() {
        adapter = new TabLayoutProfileFragment(getChildFragmentManager());

        RecProfileFragment blog = new RecProfileFragment(0);
//        RecProfileFragment miniblog = new RecProfileFragment(1);

        adapter.addFragment(blog,"");
//        adapter.addFragment(miniblog,"MINI BLOGS");

        viewPager.setAdapter(adapter);
//        tabLayout.setupWithViewPager(viewPager);
    }

    private void cast() {
        sign_out = view.findViewById(R.id.sign_out);
        tv_bio = view.findViewById(R.id.bio);
        tv_username = view.findViewById(R.id.username);
        user_img = view.findViewById(R.id.user_img);
//        tabLayout = view.findViewById(R.id.tabLayout_profile);
        viewPager = view.findViewById(R.id.view_pager_profile);
        NestedScrollView nsv = view.findViewById(R.id.NSV);
        nsv.setFillViewport(true);


        DataBase db = new DataBase(getActivity());
        bio = db.getBio();
        email = db.getEmail();
        name =db.getName();

        tv_username.setText(name);
        tv_bio.setText(bio);

        if (GetBlog_Adopter.MY_IMG.trim().length() != 0 && GetBlog_Adopter.MY_IMG != null){
//            Glide.with(getContext()).load(GetBlog_Adopter.MY_IMG).into( user_img ); todo : get in server
        }
    }

    private void signOut() {
        sign_out.setOnClickListener(v -> {

            DataBase db = new DataBase(getContext());
            db.logout();
            Intent intent = new Intent(getContext(), Splash.class);
            startActivity(intent);
            requireActivity().finish();
        });

    }

    private void ImgPro(){
        APIInterface request = APIClient.getAPIListClient(APIClient.url).create(APIInterface.class);
        DataBase db = new DataBase(getContext());
        Call call = request.getprofile(APIInterface.auth,db.getId());

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                if (response.code() == 200 || response.code() == 201){

                    if ( response.body().get(0).getImgPro() != null && !response.body().get(0).getImgPro().equals("")) {
                        Glide.with(getContext()).load(response.body().get(0).getImgPro()).into(user_img);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }

}
