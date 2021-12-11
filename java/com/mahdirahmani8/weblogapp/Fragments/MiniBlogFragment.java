package com.mahdirahmani8.weblogapp.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mahdirahmani8.API.APIClient;
import com.mahdirahmani8.API.APIInterface;
import com.mahdirahmani8.Model.AllMiniBlogs;
import com.mahdirahmani8.weblogapp.Adopter.MiniBlogAdopter;
import com.mahdirahmani8.weblogapp.DataBase.DataBase;
import com.mahdirahmani8.weblogapp.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MiniBlogFragment extends Fragment {

    View view;
    APIInterface request;
    AllMiniBlogs miniBlogList = new AllMiniBlogs();
    MiniBlogAdopter adapter;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    RecyclerRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.mini_blog_fragment, container, false);
        request = APIClient.getAPIListClient(APIClient.url).create(APIInterface.class);
        cast();
        getMiniBlogs();
        refresh();


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setCancelable(false);
                View vv = getLayoutInflater().inflate(R.layout.miniblog_dialog, null, false);

                EditText et = vv.findViewById(R.id.et_mini_dialog);
                Button success = vv.findViewById(R.id.btn_dialog_success);
                Button cancel = vv.findViewById(R.id.btn_dialog_cancel);
                builder.setView(vv);

                AlertDialog dialog = builder.create();
                dialog.show();

                success.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        DataBase db = new DataBase(getContext());
                        Call<ResponseBody> call = request.postMyMiniBlog(APIInterface.auth,et.getText().toString().trim(),db.getId());
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                if (response.code() == 200 || response.code() == 201 ) {
                                    refresh();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

        return view;
    }

    private void refresh() {
        refreshLayout.setOnRefreshListener(new RecyclerRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMiniBlogs();
            }
        });
    }

    private void getMiniBlogs() {
        DataBase db = new DataBase(getContext());
        Call<AllMiniBlogs> call = request.getAllMiniBlog(APIInterface.auth,"-date");

        call.enqueue(new Callback<AllMiniBlogs>() {
            @Override
            public void onResponse(Call<AllMiniBlogs> call, Response<AllMiniBlogs> response) {
                if (response.code() == 200) {
                    miniBlogList = response.body();

                    if (getContext() != null) {

                        assert miniBlogList != null;
                        adapter = new MiniBlogAdopter(miniBlogList, getContext());


                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                        refreshLayout.setRefreshing(false);
                    }

                } else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AllMiniBlogs> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage() , Toast.LENGTH_LONG).show();
                getMiniBlogs();
            }
        });
    }

    private void cast() {

        progressBar = view.findViewById(R.id.spin_kit);
        refreshLayout = view.findViewById(R.id.refresh_layout_mini);
        Sprite wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);

        fab = view.findViewById(R.id.FAB);
        recyclerView = view.findViewById(R.id.REC_mini_blog);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // hide and show
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    fab.hide();
                } else {
                    fab.show();
                }
            }

        });
    }


}
