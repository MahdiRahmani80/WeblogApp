package com.mahdirahmani8.weblogapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mahdirahmani8.API.APIClient;
import com.mahdirahmani8.API.APIInterface;
import com.mahdirahmani8.Model.NameBioImg_User;
import com.mahdirahmani8.Model.User;
import com.mahdirahmani8.weblogapp.DataBase.DataBase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetName extends AppCompatActivity {

    TextView submit, txt_img;
    ImageView camera;
    CircleImageView circleImageView;
    RelativeLayout rel_photo;
    EditText et_name, et_bio;
    final int Select = 1;
    private String IMG_PATH;
    private static final String IMAGE_DIRECTORY = "WeblogApp";
    Uri imageUri;
    String url = "http://weblogapp0.pythonanywhere.com/";
    APIInterface request;

    @SuppressLint("IntentReset")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_name);

        cast();
        circleImageView.setVisibility(View.GONE);
        // request file
        request = APIClient.getAPIListClient(url).create(APIInterface.class);

        // submit set onClick
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = getIntent();
                String email = i.getExtras().getString("EMAIL");
                String name = et_name.getText().toString().trim();
                String bio = et_bio.getText().toString().trim();

                // write in DataBase
                DataBase db = new DataBase(GetName.this);
                db.setProfile(email, name, bio);

                File file = null;
                if (IMG_PATH != null && IMG_PATH.trim().length() != 0) {
                    file = new File(IMG_PATH);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                    Date d = new Date();
                    d.getTime();
                    MultipartBody.Part img_upload = MultipartBody.Part.createFormData(
                            "imgPro",
                            file.getName(),
                            requestBody
                    );

                    Call<NameBioImg_User> call = request.sendBio(
                            email,
                            Credentials.basic("mahdi", "M@hdi1380*"),
                            name,
                            bio,
                            img_upload
                    );

                    call.enqueue(new Callback<NameBioImg_User>() {
                        @Override
                        public void onResponse(Call<NameBioImg_User> call, Response<NameBioImg_User> response) {

                            Toast.makeText(GetName.this, response.message(), Toast.LENGTH_LONG).show();
                            getUserINFO(email);

                            Intent intent = new Intent(GetName.this, MianActivity.class);
                            startActivity(intent);
                            finish();

                        }

                        @Override
                        public void onFailure(Call<NameBioImg_User> call, Throwable t) {
                            Toast.makeText(GetName.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("Error send bio", t.getMessage());
                        }
                    });


                } else {

                    Call<NameBioImg_User> call = request.sendBio(
                            email,
                            Credentials.basic("mahdi", "M@hdi1380*"),
                            name,
                            bio
                    );


                    call.enqueue(new Callback<NameBioImg_User>() {
                        @Override
                        public void onResponse(Call<NameBioImg_User> call, Response<NameBioImg_User> response) {

                            Toast.makeText(GetName.this, response.message(), Toast.LENGTH_LONG).show();
                            getUserINFO(email);

                            Intent intent = new Intent(GetName.this, MianActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<NameBioImg_User> call, Throwable t) {
                            Toast.makeText(GetName.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("Error send bio", t.getMessage());
                        }
                    });
                }
            }
        });

        // Relative layout image set on click
        rel_photo.setOnClickListener(v -> {
            camera.setVisibility(View.GONE);
            txt_img.setVisibility(View.GONE);
            circleImageView.setVisibility(View.VISIBLE);

            @SuppressLint("IntentReset")
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            i.setType("image/*");
            startActivityForResult(i, Select);
        });

    }

    private void getUserINFO(String email) {
        Call<List<User>> call = request.getMyINFO(APIInterface.auth, email);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.code() == 200) {
                    DataBase db = new DataBase(getApplicationContext());
                    assert response.body() != null;
                    db.setId(response.body().get(0).getId());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            return;
        }

        if (requestCode == Select) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                Uri select = data.getData();
                InputStream input = null;

                try {
                    assert select != null;
//                    input = getContentResolver().openInputStream(select);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), select);
                    circleImageView.setImageBitmap(bitmap);
                    IMG_PATH = saveImage(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }

//                BitmapFactory.decodeStream(input);
//                imageUri = select;
//                circleImageView.setImageURI(select);
            }
        }
    }

    private String saveImage(Bitmap myBitmap) throws IOException {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File images = new File(Environment.getExternalStorageDirectory()+ File.separator+ IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!images.exists()) {
            images.mkdirs();
        }

        File f = new File(images, Calendar.getInstance().getTimeInMillis() + ".jpg");
        FileOutputStream fo = new FileOutputStream(f);
        f.createNewFile();
        fo.write(bytes.toByteArray());
        MediaScannerConnection.scanFile(
                this,
                new String[]{f.getPath()},
                new String[]{"image/jpg"}, null);
        fo.flush();
        fo.close();
        Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

        return f.getAbsolutePath();

    }


//    public String getRealPathFromURI(Uri uri) {
//
//        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//        return cursor.getString(idx);
//    }


    private void cast() {
        submit = (TextView) findViewById(R.id.submit);
        rel_photo = (RelativeLayout) findViewById(R.id.REL_photo);
        camera = (ImageView) findViewById(R.id.user_img);
        circleImageView = (CircleImageView) findViewById(R.id.circle_img);
        txt_img = (TextView) findViewById(R.id.txt_img);
        et_name = (EditText) findViewById(R.id.name);
        et_bio = (EditText) findViewById(R.id.bio);
    }

}