package com.mahdirahmani8.weblogapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mahdirahmani8.API.APIClient;
import com.mahdirahmani8.API.APIInterface;
import com.mahdirahmani8.Model.User;
import com.mahdirahmani8.Model.Verify;
import com.mahdirahmani8.weblogapp.DataBase.DataBase;

import java.util.List;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPass2 extends AppCompatActivity {

    EditText et_pass;
    TextView confirm;
    APIInterface request;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass2);

        cast();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        }) ;

    }

    private void sendRequest() {
        request = APIClient.getAPIListClient(APIClient.url).create(APIInterface.class);

         Call <List<Verify>> call = request.login(
                Credentials.basic("mahdi", "M@hdi1380*"),
                email,
                et_pass.getText().toString().trim()
         );

         call.enqueue(new Callback<List<Verify>>() {
             @Override
             public void onResponse(Call<List<Verify>> call, Response<List<Verify>> response) {

                 boolean bool =false;
                 if (response.code() == 200 && response.body() != null && response.body().size() != 0 ){
                     bool = response.body().get(0).isVerified();
                     if (bool){

                         Toast.makeText(ForgotPass2.this,"Welcome" ,Toast.LENGTH_LONG).show();
                         DataBase db =new DataBase(ForgotPass2.this);
                         db.login();
                         getUserINFO(email);

                         Intent intent = new Intent(ForgotPass2.this,MianActivity.class);
                         startActivity(intent);
                         finish();
                     }

                 } else if (response.code() == 200) {

                     Toast.makeText(ForgotPass2.this,"Your email or password incorrect ",Toast.LENGTH_LONG).show();
                     et_pass.setText("");

                 } else {
                     Toast.makeText(ForgotPass2.this,"We have some error !" ,Toast.LENGTH_LONG);
                 }

             }

             @Override
             public void onFailure(Call<List<Verify>> call, Throwable t) {
                Toast.makeText(ForgotPass2.this,t.getMessage(),Toast.LENGTH_LONG).show();
             }
         });

    }

    private void cast() {

        et_pass = findViewById(R.id.et_code);
        confirm = findViewById(R.id.verify);

        Intent intent = getIntent();
        email = intent.getStringExtra("EMAIL");

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ForgotPass2.this,SignIn.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    private void getUserINFO(String email) {
        Call<List<User>> call = request.getMyINFO(APIInterface.auth, email);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.code() == 200){
                    DataBase db = new DataBase(getApplicationContext());
                    db.setId(response.body().get(0).getId());
                    // update profile
                    db.setProfile(
                            email,
                            response.body().get(0).getName(),
                            response.body().get(0).getBio()
                    );
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }
}