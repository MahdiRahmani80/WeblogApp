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
import com.mahdirahmani8.Model.ForgotPass;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Forgot_pass extends AppCompatActivity {

    TextView submit;
    EditText email;
    APIInterface request;
    String url = "http://weblogapp0.pythonanywhere.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        cast();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendRequest();
            }
        });

    }

    private void sendRequest() {
        request = APIClient.getAPIListClient(url).create(APIInterface.class);

        Call<ForgotPass> call = request.forgot(
                Credentials.basic("mahdi", "M@hdi1380*"),
                email.getText().toString().trim(),
                true
        );

        call.enqueue(new Callback<ForgotPass>() {
            @Override
            public void onResponse(Call<ForgotPass> call, Response<ForgotPass> response) {

                if (response.code() == 200){

                    Toast.makeText(Forgot_pass.this,"Please check your email inbox",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Forgot_pass.this, ForgotPass2.class);
                    intent.putExtra("FORGOT",true);
                    intent.putExtra("EMAIL",email.getText().toString().trim() );
                    startActivity(intent);
                    finish();

                }else {
                    Toast.makeText(Forgot_pass.this,response.message(),Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ForgotPass> call, Throwable t) {
                Toast.makeText(Forgot_pass.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cast() {
        submit = (TextView) findViewById(R.id.send_mail);
        email = (EditText) findViewById(R.id.forgot_pass_email) ;
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Forgot_pass.this,SignIn.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}