package com.mahdirahmani8.weblogapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mahdirahmani8.API.APIClient;
import com.mahdirahmani8.API.APIInterface;
import com.mahdirahmani8.Model.Register;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUp extends AppCompatActivity {

    TextView tv_signIn;
    EditText confirmPass, pass, email, name;
    TextView signUpButton;
    String url = "http://weblogapp0.pythonanywhere.com/";
    APIInterface request;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        cast();
        request = APIClient.getAPIListClient(url).create(APIInterface.class);

        tv_signIn.setOnClickListener(v -> {

            Intent intent = new Intent(SignUp.this, SignIn.class);
            startActivity(intent);
            finish();
        });


        signUpButton.setOnClickListener(v -> createAccount());

    }

    private void createAccount() {
        if ((pass.getText().toString().hashCode() == confirmPass.getText().toString().hashCode())) {

            sendRequest();

        } else if (pass.getText().toString().hashCode() != confirmPass.getText().toString().hashCode()) {
            Toast.makeText(SignUp.this, "your password confirm incorrect", Toast.LENGTH_LONG).show();
            Toast.makeText(SignUp.this, pass.getText().toString(), Toast.LENGTH_LONG).show();
        } else {

            Toast.makeText(SignUp.this, "We have some error !", Toast.LENGTH_LONG).show();
        }

    }

    private void sendRequest() {

        String mail = email.getText().toString();

        Call<Register> call = request.createUser(
                Credentials.basic("mahdi", "M@hdi1380*"),mail, pass.getText().toString());

        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {

                if (response.code() == 201) {
                    Toast.makeText(SignUp.this, "please check you email ", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(SignUp.this, VeryfyEmail.class);
                    intent.putExtra("FORGOT", false);
                    intent.putExtra("EMAIL", email.getText().toString().trim());
                    startActivity(intent);
                    finish();

                } else if (response.code() == 400) {
                    Toast.makeText(SignUp.this, " Email exist ", Toast.LENGTH_LONG).show();
                    pass.setText("");
                    confirmPass.setText("");
                } else {
                    Toast.makeText(SignUp.this, " code : " + response.code(), Toast.LENGTH_LONG).show();
                    pass.setText("");
                    confirmPass.setText("");
                }

            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {

            }
        });

    }

    private void cast() {

        tv_signIn = (TextView) findViewById(R.id.txt);
        email = (EditText) findViewById(R.id.et_username);
        pass = (EditText) findViewById(R.id.et_password);
        confirmPass = (EditText) findViewById(R.id.et_re_password);
        signUpButton = (TextView) findViewById(R.id.login_btn);
    }

    boolean bool= false;

    @Override
    public void onBackPressed() {
        if (bool) {
            super.onBackPressed();
        } else{
            bool = true;
        }
    }
}