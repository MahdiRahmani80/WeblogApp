package com.mahdirahmani8.weblogapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mahdirahmani8.API.APIClient;
import com.mahdirahmani8.API.APIInterface;
import com.mahdirahmani8.Model.GetCode;
import com.mahdirahmani8.Model.Register;
import com.mahdirahmani8.Model.User;
import com.mahdirahmani8.Model.Verify;
import com.mahdirahmani8.weblogapp.DataBase.DataBase;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VeryfyEmail extends AppCompatActivity {

    EditText verify_code;
    TextView submit;
    String url = "http://weblogapp0.pythonanywhere.com/";
    String email;
    APIInterface request;
    List<GetCode> getCodeList = new ArrayList<GetCode>();
    String password;
    boolean isForgrtPass, resendCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veryfy_email);

// retrofit
        request = APIClient.getAPIListClient(url).create(APIInterface.class);

        cast();

        verify_code.setOnClickListener(v -> {

        });

        submit.setOnClickListener(v -> {

            if (!isForgrtPass) {

                // if your verified go to Main
                verifyCode();

            } else {

                Intent intent = new Intent(VeryfyEmail.this, MianActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void verify_txt() {

        Call<Verify> call = request.verify(email, Credentials.basic("mahdi", "M@hdi1380*"), true);
        call.enqueue(new Callback<Verify>() {
            @Override
            public void onResponse(Call<Verify> call, Response<Verify> response) {

                if (response.code() == 200) {
                    Toast.makeText(VeryfyEmail.this, ":)", Toast.LENGTH_LONG).show();

                    // create login in local
                    DataBase db = new DataBase(VeryfyEmail.this);
                    db.login();
                    getUserINFO(email);


                } else {
                    Toast.makeText(VeryfyEmail.this, response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Verify> call, Throwable t) {

            }
        });
    }

    private void verifyCode() {

//        int verify_txt = new Integer(verify_code.getText().toString());   // Don't use because we are will get this
        Call<List<GetCode>> call = request.getCode(email, Credentials.basic("mahdi", "M@hdi1380*"));

        call.enqueue(new Callback<List<GetCode>>() {
            @Override
            public void onResponse(Call<List<GetCode>> call, Response<List<GetCode>> response) {

                if (response.code() == 200) {
//                    Toast.makeText(VeryfyEmail.this, "verified :)", Toast.LENGTH_LONG).show();
                    getCodeList = response.body();

                    // when response is 200 send is verified is true
                    if (verify_code.getText().toString().hashCode() != 0 &&
                            new Integer(verify_code.getText().toString()) == getCodeList.get(0).getVerify_code()) {
                        verify_txt();
                        // change activity
                        Intent intent = new Intent(VeryfyEmail.this, GetName.class);
                        intent.putExtra("EMAIL", email);
                        startActivity(intent);
                        finish();
                    } else {

                        Toast.makeText(VeryfyEmail.this, "your code is incorrect", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(VeryfyEmail.this, "We have some Error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<GetCode>> call, Throwable t) {
                Toast.makeText(VeryfyEmail.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    private void cast() {
        verify_code = (EditText) findViewById(R.id.et_code);
        submit = (TextView) findViewById(R.id.verify);

        Intent get = getIntent();
        resendCode = get.getBooleanExtra("RESEND_EMAIL", false);
        isForgrtPass = get.getBooleanExtra("FORGOT", false);
        password = get.getStringExtra("PASS");
        email = get.getStringExtra("EMAIL");

        if (resendCode) {
            resendEmail(password);
        }
    }

    private void resendEmail(String password) {
        Call<Register> call = request.resend_email(
                Credentials.basic("mahdi", "M@hdi1380*"),
                email,
                email,
                password
        );


        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {

                if (response.code() == 200) {
                    Toast.makeText(VeryfyEmail.this, "Please check your Email inbox", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(VeryfyEmail.this, "Request Code : " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Toast.makeText(VeryfyEmail.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    private void getUserINFO(String email) {
        Call<List<User>> call = request.getMyINFO(APIInterface.auth, email);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.code() == 200){
                    DataBase db = new DataBase(getApplicationContext());
                    assert response.body() != null;
                    db.setId(response.body().get(0).getId());
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