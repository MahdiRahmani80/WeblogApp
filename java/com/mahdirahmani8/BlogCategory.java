package com.mahdirahmani8;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mahdirahmani8.API.APIClient;
import com.mahdirahmani8.API.APIInterface;
import com.mahdirahmani8.Model.Blog;
import com.mahdirahmani8.weblogapp.DataBase.DataBase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlogCategory extends AppCompatActivity {

//    RecyclerView recyclerView;
//    LinearLayout send_blog;
//    ShowCategoriesAdapter adapter;
//    APIInterface request;
//    String txt_blog,sub,date;
//    boolean isGetTmp;
//    int tmp_id,from_Activity,republish_id,like,id;


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_blog_category);
//
//        cast();
////        getCategory();
//
////        if (from_Activity == 8){
//////            send_blog.setOnClickListener(v -> send_edit_blog(id));
////        }else {
//////            send_blog.setOnClickListener(v -> sendBlog(txt_blog));
////        }
//    }
//
//    private void cast() {
//        recyclerView = findViewById(R.id.rec_category);
//        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
//        send_blog = findViewById(R.id.lin_send_blog);
//
//        Intent intent = getIntent();
//        txt_blog = intent.getExtras().getString("TEXT");
//        isGetTmp = intent.getExtras().getBoolean("IS_GET_TMP");
//        tmp_id =  intent.getExtras().getInt("TMP_ID");
//        from_Activity = intent.getExtras().getInt("FROM");
//        sub = intent.getExtras().getString("SUB");
//        republish_id = intent.getExtras().getInt("REP_ID");
//        like = intent.getExtras().getInt("LIKE");
//        id = intent.getExtras().getInt("ID");
//        date = intent.getExtras().getString("DATE");
//
//
//    }

    public static void send_edit_blog(int id,String sub,String txt_blog,String date,int like, Context context) {
        APIInterface request;
        request = APIClient.getAPIListClient(APIClient.url).create(APIInterface.class);
        DataBase db = new DataBase(context);

        Call<Blog> call = request.editBlog(APIInterface.auth,
                id,
                sub,
                txt_blog,
                db.getId(),
                null,
                date,
                like
        );

        call.enqueue(new Callback<Blog>() {
            @Override
            public void onResponse(Call<Blog> call, Response<Blog> response) {
                Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                if (response.code() == 200 || response.code() == 201){
//                    Intent intent = new Intent(context, MianActivity.class);
//                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Blog> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

//    private void getCategory() {
//        request = APIClient.getAPIListClient(APIClient.url).create(APIInterface.class);
//
//        Call<List<Category>> call = request.getCategories(APIInterface.auth);
//
//        call.enqueue(new Callback<List<Category>>() {
//            @Override
//            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
//
//                if (response.code() == 200) {
//                    adapter = new ShowCategoriesAdapter(getApplicationContext(), response.body());
//                    recyclerView.setAdapter(adapter);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Category>> call, Throwable t) {
//
//            }
//        });
//    }

    public static void sendBlog(Context context,int from_Activity,String sub,int republish_id,String text,boolean isGetTmp,int tmp_id) {

        APIInterface request;
        request = APIClient.getAPIListClient(APIClient.url).create(APIInterface.class);

        DataBase db = new DataBase(context);
        Call<Blog> call ;
        if (from_Activity ==0) {
            call = request.sendBlog(APIInterface.auth, sub, text, db.getId(),republish_id,null);
        } else {
            call = request.sendBlog(APIInterface.auth, sub , text, db.getId(), null);
        }

        call.enqueue(new Callback<Blog>() {
            @Override
            public void onResponse(Call<Blog> call, Response<Blog> response) {
                if (response.code() == 201 || response.code() == 200) {
                    Toast.makeText(context, "0k", Toast.LENGTH_SHORT).show();

                    if (isGetTmp){

                        DataBase db= new DataBase(context);
                        db.deleteTempBlog(tmp_id);
                    }

                } else {
                    Toast.makeText(context, response.code() + "" /*"We have some Error!!"*/, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Blog> call, Throwable t) {

            }
        });
    }

}