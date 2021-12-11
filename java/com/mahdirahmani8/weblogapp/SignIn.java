package com.mahdirahmani8.weblogapp;

import android.content.Intent;
import android.os.Bundle;
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


public class SignIn extends AppCompatActivity {

    TextView tv_signUp, forgotPass;
    // TODO : replace phone to email (just name)
    EditText phone, pass;
    TextView loginButton;
    APIInterface request;
    String url = "http://weblogapp0.pythonanywhere.com/";
    boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cast();
        request = APIClient.getAPIListClient(url).create(APIInterface.class);


        tv_signUp.setOnClickListener(v -> {

            Intent intent = new Intent(SignIn.this, SignUp.class);
            startActivity(intent);
            finish();
        });

        loginButton.setOnClickListener(v -> isSubmitOnClick());

        forgotPass.setOnClickListener(v -> {
            Intent intent = new Intent(SignIn.this, Forgot_pass.class);
//                intent.putExtra("FORGET_PASS" , true);
            startActivity(intent);
            finish();
        });
    }

    private void isSubmitOnClick() {

        String email = phone.getText().toString().trim() ;
        String password = pass.getText().toString().trim();

        if (email.hashCode() == 0 || password.hashCode() == 0 ) {

            Toast.makeText(SignIn.this,"Email or password can't empty",Toast.LENGTH_LONG).show();
        }else {
            getIsVerified(email, password);
        }
    }

    private void cast() {
        tv_signUp = (TextView) findViewById(R.id.txt);
        forgotPass = (TextView) findViewById(R.id.forgot_pass);
        phone = (EditText) findViewById(R.id.et_phone);
        pass = (EditText) findViewById(R.id.et_password);
        loginButton = (TextView) findViewById(R.id.login_btn);
    }


    private void getIsVerified(String email,String password ) {

        Call<List<Verify>> call = request.login(
                Credentials.basic("mahdi", "M@hdi1380*"),
                email, password
        );

        call.enqueue(new Callback<List<Verify>>() {
            @Override
            public void onResponse(Call<List<Verify>> call, Response<List<Verify>> response) {

                if (response.code() == 200) {

                    if (response.body() != null && response.body().size() != 0 ) {
                        isLogin = response.body().get(0).isVerified();

                        if (isLogin) {
                            Toast.makeText(SignIn.this, "Welcome", Toast.LENGTH_LONG).show();

                            DataBase db = new DataBase(SignIn.this);
                            db.login();
                            getUserINFO(phone.getText().toString().trim());


                        } else {
                            goToNextActivity();
                        }


                    } else {
                        Toast.makeText(SignIn.this, "Your email or password incorrect", Toast.LENGTH_LONG).show();
                        pass.setText("");
//                        goToNextActivity();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Verify>> call, Throwable t) {
                Toast.makeText(SignIn.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });


    }


    private void goToNextActivity() {

        Toast.makeText(SignIn.this, "your account is not Verified !", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(SignIn.this, VeryfyEmail.class);
        intent.putExtra("RESEND_EMAIL", true);
        intent.putExtra("PASS", pass.getText().toString().trim());
        intent.putExtra("EMAIL", phone.getText().toString().trim());
        startActivity(intent);
        finish();

    }

    boolean bool= false;

    @Override
    public void onBackPressed() {
        if (bool) {
            super.onBackPressed();
//            Toast.makeText(SignIn.this,"",Toast.LENGTH_LONG).show();

        } else{
            bool = true;
        }
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
                    // update profile
                    db.setProfile(
                            email,
                            response.body().get(0).getName(),
                            response.body().get(0).getBio()
                    );

                    // Go to HomeFragment ==>
                    Intent intent = new Intent(SignIn.this, MianActivity.class);
                    startActivity(intent);
                    finish();

                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }
}