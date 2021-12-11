package com.mahdirahmani8.weblogapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.mahdirahmani8.API.APIClient;
import com.mahdirahmani8.API.APIInterface;
import com.mahdirahmani8.Model.Blog;
import com.mahdirahmani8.Model.HomeAllBlog;
import com.mahdirahmani8.weblogapp.Adopter.GetBlog_Adopter;
import com.mahdirahmani8.weblogapp.DataBase.DataBase;
import com.mahdirahmani8.weblogapp.R;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    View view;
    RecyclerRefreshLayout layout;
    RecyclerView recyclerView;
    APIInterface request;
    GetBlog_Adopter adopter;
    HomeAllBlog blogList,blogListS;
    ProgressBar progressBar;
    int page = 1, searchPage = 1;
    SpinKitView load;
    MaterialSearchBar msb;
    boolean isFirstSc,isSearching;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.home_fragment, container, false);
        cast();

        refresh();
        getBlogs();

        msb.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                isSearching = enabled;

                if (!enabled){
                    adopter = new GetBlog_Adopter(getContext(),blogList);
                    recyclerView.setAdapter(adopter);
                }

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                request = APIClient.getAPIListClient(APIClient.url).create(APIInterface.class);
                Call<HomeAllBlog> call = request.getSearch(APIInterface.auth, text.toString().trim(), 1);

                call.enqueue(new Callback<HomeAllBlog>() {
                    @Override
                    public void onResponse(Call<HomeAllBlog> call, Response<HomeAllBlog> response) {
                        if (getContext() != null && response.code() == 200) {
                            adopter = new GetBlog_Adopter(getContext(), response.body());
                            recyclerView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setAdapter(adopter);

                            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);

                                    if (!recyclerView.canScrollVertically(1)) {
                                        searchPage++;
                                        load.setVisibility(View.VISIBLE);
                                        Call<HomeAllBlog> c = request.getSearch(APIInterface.auth, text.toString().trim(), searchPage);

                                        c.enqueue(new Callback<HomeAllBlog>() {
                                            @Override
                                            public void onResponse(Call<HomeAllBlog> call, Response<HomeAllBlog> response) {
                                                if (response.code() == 200 && getContext() != null) {
                                                    load.setVisibility(View.GONE);
                                                    ArrayList<Blog> blogs = new ArrayList<>();

                                                    for (int i = 0; i < blogListS.getResults().size(); i++) {
                                                        blogs.add(blogListS.getResults().get(i));
                                                    }
                                                    for (int j = 0; j < response.body().getResults().size(); j++) {
                                                        blogs.add(response.body().getResults().get(j));
                                                    }

                                                    HomeAllBlog h = new HomeAllBlog();
                                                    h.setResults(blogs);
                                                    h.setCount(blogList.getCount());
                                                    h.setNext(response.body().getNext());
                                                    h.setPrevious(response.body().getPrevious());

                                                    GetBlog_Adopter.change(h);
                                                    recyclerView.setAdapter(adopter);
                                                    recyclerView.scrollToPosition(response.body().getCount() - response.body().getResults().size() - 1);

                                                } else if (getContext() != null) {
                                                    Toast.makeText(getContext(), "First Blog!", Toast.LENGTH_SHORT).show();
                                                    load.setVisibility(View.GONE);
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<HomeAllBlog> call, Throwable t) {
                                                load.setVisibility(View.GONE);
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<HomeAllBlog> call, Throwable t) {
                        adopter = new GetBlog_Adopter(getContext(), blogListS);
                        recyclerView.setAdapter(adopter);
                    }
                });
            }

            @Override
            public void onButtonClicked(int buttonCode) {


            }
        });


        return view;
    }


    private void cast() {

        progressBar = view.findViewById(R.id.spin_kit);
        Sprite wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);

        layout = view.findViewById(R.id.refresh_layout);
        recyclerView = view.findViewById(R.id.REC_home);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        NestedScrollView nsv = view.findViewById(R.id.nsv);
        nsv.setFillViewport(true);
        load = view.findViewById(R.id.load);
        msb = view.findViewById(R.id.search);
        msb.setSearchIcon(R.drawable.search);
    }

    private void refresh() {
        layout.setOnRefreshListener(this::getBlogs);
    }
// GET BLOG IN FEED
//    private void getBlogs() {
//
//        request = APIClient.getAPIListClient(APIClient.url).create(APIInterface.class);
//        int id;
//        DataBase db = new DataBase(getContext());
//        id = db.getId();
//        Call<List<Feed>> call= request.getBlog(APIInterface.auth, id, "json");
//
//        new Thread(() -> requireActivity().runOnUiThread(() ->
//
//                call.enqueue(new Callback<List<Feed>>() {
//                    @Override
//                    public void onResponse(Call<List<Feed>> call, Response<List<Feed>> response) {
//
//                        if (response.code() == 200) {
//
//                            progressBar.setVisibility(View.GONE);
//                            if (getContext() != null) {
//                                blogList = response.body();
//                                adopter = new GetBlog_Adopter(getContext(), blogList);
//
//                                recyclerView.setAdapter(adopter);
//                                layout.setRefreshing(false); // refreshing page to false
//                            }
//
//                        } else if (getContext() != null) {
//                            Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<Feed>> call, Throwable t) {
//                        getBlogs();
//                    }
//
//                }))).start();
//    }

    private void getBlogs() {
        request = APIClient.getAPIListClient(APIClient.url).create(APIInterface.class);
        int id;
        DataBase db = new DataBase(getContext());
        id = db.getId();
        Call call = request.getBlogs(APIInterface.auth, "-date", 1);

        call.enqueue(new Callback<HomeAllBlog>() {
            @Override
            public void onResponse(Call<HomeAllBlog> call, Response<HomeAllBlog> response) {
                if (response.code() == 200) {

                    progressBar.setVisibility(View.GONE);
                    if (getContext() != null) {
                        blogList = response.body();
                        adopter = new GetBlog_Adopter(getContext(), blogList);

                        recyclerView.setAdapter(adopter);
                        layout.setRefreshing(false); // refreshing page to false

                        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);

                                if (!recyclerView.canScrollVertically(1) && !isFirstSc && !isSearching) {

                                    page++;
//                            Toast.makeText(getContext(), "Wait ...", Toast.LENGTH_LONG).show();
                                    load.setVisibility(View.VISIBLE);
                                    Call<HomeAllBlog> c = request.getBlogs(APIInterface.auth, "-date", page);
                                    c.enqueue(new Callback<HomeAllBlog>() {
                                        @Override
                                        public void onResponse(Call<HomeAllBlog> call, Response<HomeAllBlog> response) {
                                            if (response.code() == 200 && getContext() != null) {
                                                load.setVisibility(View.GONE);
                                                ArrayList<Blog> blogs = new ArrayList<>();

                                                for (int i = 0; i < blogList.getResults().size(); i++) {
                                                    blogs.add(blogList.getResults().get(i));
                                                }
                                                for (int j = 0; j < response.body().getResults().size(); j++) {
                                                    blogs.add(response.body().getResults().get(j));
                                                }

                                                HomeAllBlog h = new HomeAllBlog();
                                                h.setResults(blogs);
                                                h.setCount(blogList.getCount());
                                                h.setNext(response.body().getNext());
                                                h.setPrevious(response.body().getPrevious());

                                                GetBlog_Adopter.change(h);
                                                recyclerView.setAdapter(adopter);
                                                recyclerView.scrollToPosition(response.body().getCount() - response.body().getResults().size() - 1);

                                            } else if (getContext() != null) {
                                                Toast.makeText(getContext(), "First Blog!", Toast.LENGTH_LONG).show();
                                                isFirstSc = true;
                                                load.setVisibility(View.GONE);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<HomeAllBlog> call, Throwable t) {
                                            Toast.makeText(getContext(), "First Blog!", Toast.LENGTH_LONG).show();
                                            isFirstSc = true;
                                            load.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            }
                        });

                    }

                } else if (getContext() != null) {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<HomeAllBlog> call, Throwable t) {

            }
        });
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setAdapter(adopter);
        if (GetBlog_Adopter.pos < 20) {
            recyclerView.scrollToPosition(GetBlog_Adopter.pos);
        }
    }


}
