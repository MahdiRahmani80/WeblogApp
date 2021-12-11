package com.mahdirahmani8.weblogapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mahdirahmani8.API.APIClient;
import com.mahdirahmani8.API.APIInterface;
import com.mahdirahmani8.Model.Category;
import com.mahdirahmani8.weblogapp.Adopter.ShowCategoriesAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowHashtag extends AppCompatActivity {

    TextView submit;
    RecyclerView rec_category;
    ShowCategoriesAdapter adapter;
    APIInterface request;
    String txt_blog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_hashtag);

        cast();

        submit.setOnClickListener(v -> {
            Intent intent = new Intent(FollowHashtag.this, MianActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void cast() {
        submit = findViewById(R.id.submit);
        rec_category = findViewById(R.id.rec_category);
        rec_category.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));

        getCategory();

    }

    private void getCategory() {
        request = APIClient.getAPIListClient(APIClient.url).create(APIInterface.class);

        Call<List<Category>> call = request.getCategories(APIInterface.auth);

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {

                if (response.code() ==200){
                    adapter = new ShowCategoriesAdapter(getApplicationContext(),response.body());
                    rec_category.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });

    }
}