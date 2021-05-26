package com.example.craftsman;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class AddPost extends AppCompatActivity {

    ImageView work_image, certificate_image, home_image;
    EditText title, price, description;
    Button certificate_btn, btn_save;
    Spinner address, category;
    CheckBox check;

    SessionManager sessionManager;
    String getId;


    String work_img;
    String certificate_img;

    Bitmap bitmap;

    private static final String WORK_POST_URL = "https://aboodbarqawi.000webhostapp.com/craftman/addpostwork.php";
    private static final String SERVICE_POST_URL = "https://aboodbarqawi.000webhostapp.com/craftman/addpostservice.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);


        work_image = findViewById(R.id.work_image);
        certificate_image = findViewById(R.id.certificate_image);
        home_image = findViewById(R.id.home_image);
        title = findViewById(R.id.title);
        price = findViewById(R.id.price);
        description = findViewById(R.id.desc);
        certificate_btn = findViewById(R.id.certificate_btn);
        btn_save = findViewById(R.id.save);
        address = findViewById(R.id.city);
        category = findViewById(R.id.category);
        check = findViewById(R.id.check_btn);
        certificate_image.setVisibility(View.GONE);


        sessionManager =  new SessionManager(this);
        sessionManager.checkLogin();

        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetail();

        getId = user.get(SessionManager.ID);

        home_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddPost.this, Categories.class);

                startActivity(i);
            }
        });
        work_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(AddPost.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent i = new Intent(Intent.ACTION_PICK);
                                i.setType("image/*");
                                startActivityForResult(Intent.createChooser(i,"Select Image"), 1);
                            }
                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                            }
                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        certificate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(AddPost.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent i = new Intent(Intent.ACTION_PICK);
                                i.setType("image/*");
                                startActivityForResult(Intent.createChooser(i,"Select Image"), 2);
                            }
                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                            }
                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPost(getId,work_img, certificate_img );
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK && data !=null){
            Uri filePath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                work_image.setImageBitmap(bitmap);
                workImage(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if(requestCode == 2 && resultCode == RESULT_OK && data !=null){
            Uri filePath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                certificate_image.setVisibility(View.VISIBLE);
                certificate_image.setImageBitmap(bitmap);
                certificateImage(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void workImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] imageBytes = stream.toByteArray();
        work_img = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
    }

    private void certificateImage(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        byte[] imageBytes = stream.toByteArray();
        certificate_img = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT);
    }

    private void addPost(String id, String workImage, String certificateImage ){

        //add the visibility ta button and ProgressBar

        btn_save.setVisibility(View.GONE);
        //get text from input views
        final String title = this.title.getText().toString().trim();
        final String description = this.description.getText().toString().trim();
        final String price = this.price.getText().toString().trim();
        final String city = this.address.getSelectedItem().toString().trim();
        final String category = this.category.getSelectedItem().toString().trim();
        boolean checked = check.isChecked();
        int checkBox_id = check.getId();
        boolean checktitle = false;
        boolean checkdescription = false;
        boolean checkcategory = false;
        boolean checkcity = false;
        boolean checkprice = false;

        //check the name if it not empty
        if(title.isEmpty()){
            this.title.setError("Name can't be Empty");
        } else if(title.length()<2){
            this.title.setError("Name too short");
        }else{
            this.title.setError(null);
            checktitle = true;
        }

        //check the name if it not empty
        if(description.isEmpty()){
            this.description.setError("Name can't be Empty");
        } else if(description.length()<2){
            this.description.setError("Name too short");
        }else{
            this.description.setError(null);
            checkdescription = true;
        }
        //check the price if it not empty
        if(price.isEmpty()){
            this.price.setError("Name can't be Empty");
        } else if(Integer.parseInt(price)<0){
            this.price.setError("Price can't be less than Zero");
        }else{
            this.price.setError(null);
            checkprice = true;
        }
        // check the password if it the same or not
        if(city.equals("Select City"))
        {
//            Toast.makeText(this,"Please select your city ",Toast.LENGTH_SHORT).show();
            Toasty.error(AddPost.this,"Please select your city ",Toast.LENGTH_SHORT,true).show();
        } else{
            checkcity = true;
        }

        // check the password if it the same or not
        if(category.equals("Select Category"))
        {
//            Toast.makeText(this,"Please select your category ",Toast.LENGTH_SHORT).show();
            Toasty.error(AddPost.this,"Please select your category ",Toast.LENGTH_SHORT,true).show();
        } else{
            checkcategory = true;
        }


        if(checked && checkBox_id==R.id.check_btn){
            if(checktitle && checkdescription && checkcity && checkcategory && checkprice)
            {
                // start the connection with the server
                // the request method post is the transfer for data from android to  php
                StringRequest stringRequest = new StringRequest(Request.Method.POST, SERVICE_POST_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            //to get the response when we pass data to the server
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            // if the data store in database will execute if and enter to home page
                            if(success.equals("1"))
                            {
//                                Toast.makeText(AddPost.this,"Added Service Successfully!",Toast.LENGTH_SHORT).show();
                                Toasty.success(AddPost.this,"Added Service Successfully!",Toast.LENGTH_SHORT,true).show();


                                Intent intent  = new Intent(AddPost.this,MainActivity.class);
                                startActivity(intent);
                            }
                            else if(success.equals("0"))
                            {
//                                Toast.makeText(AddPost.this,"Added Service  Failed! ",Toast.LENGTH_SHORT).show();
                                Toasty.error(AddPost.this,"Added Service  Failed! ",Toast.LENGTH_SHORT,true).show();

                                btn_save.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //if error occur
//                            Toast.makeText(AddPost.this,"Added Service Error!"+ e.toString(),Toast.LENGTH_SHORT).show();
                            Toasty.error(AddPost.this,"Added Service  Error!"+ e.toString(),Toast.LENGTH_SHORT,true).show();


                            btn_save.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(AddPost.this,"Added Service  Error!"+ error.toString(),Toast.LENGTH_SHORT).show();
                        Toasty.error(AddPost.this,"Added Service  Error!"+ error.toString(),Toast.LENGTH_SHORT,true).show();

                        btn_save.setVisibility(View.VISIBLE);
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        //passing data to server
                        //with params we can put data that will passing with key to identify each one to php code
                        Map<String, String> params = new HashMap<>();
                        params.put("title",title);
                        params.put("description",description);
                        params.put("address",city);
                        params.put("category",category);
                        params.put("price",price);
                        params.put("work_image",workImage);
                        params.put("id",id);


                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }
            else{

                btn_save.setVisibility(View.VISIBLE);
//                Toast.makeText(AddPost.this,"Please Check your Entries",Toast.LENGTH_SHORT).show();
                Toasty.error(AddPost.this,"Please Check your Entries",Toast.LENGTH_SHORT,true).show();

            }
        }else{
            if(checktitle && checkdescription && checkcity && checkcategory && checkprice)
            {
                // start the connection with the server
                // the request method post is the transfer for data from android to  php
                StringRequest stringRequest = new StringRequest(Request.Method.POST, WORK_POST_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            //to get the response when we pass data to the server
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            // if the data store in database will execute if and enter to home page
                            if(success.equals("1"))
                            {
//                                Toast.makeText(AddPost.this,"Added Work Successfully!",Toast.LENGTH_SHORT).show();
                                Toasty.success(AddPost.this," Added Work Successfully!", Toasty.LENGTH_LONG,true).show();
                                Intent intent  = new Intent(AddPost.this,Categories.class);
                                startActivity(intent);
                            }
                            else if(success.equals("0"))
                            {
                                Toasty.error(AddPost.this,"Added Work Failed! ",Toast.LENGTH_SHORT,true).show();

                                btn_save.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //if error occur
//                            Toast.makeText(AddPost.this,"Added Work Error!"+ e.toString(),Toast.LENGTH_SHORT).show();
                            Toasty.error(AddPost.this,"Added Work Error!"+ e.toString(),Toast.LENGTH_SHORT,true).show();


                            btn_save.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(AddPost.this,"Added Work  Error!"+ error.toString(),Toast.LENGTH_SHORT).show();
                        Toasty.error(AddPost.this,"Added Work Error!"+ error.toString(),Toast.LENGTH_SHORT,true).show();

                        btn_save.setVisibility(View.VISIBLE);
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        //passing data to server
                        //with params we can put data that will passing with key to identify each one to php code
                        Map<String, String> params = new HashMap<>();
                        params.put("title",title);
                        params.put("description",description);
                        params.put("address",city);
                        params.put("category",category);
                        params.put("price",price);
                        params.put("work_image",workImage);
                        params.put("certificate_image",certificateImage);
                        params.put("id",id);


                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }
            else{

                btn_save.setVisibility(View.VISIBLE);
//                Toast.makeText(AddPost.this,"Please Check your Entries",Toast.LENGTH_SHORT).show();
                Toasty.error(AddPost.this,"Please Check your Entries",Toast.LENGTH_SHORT,true).show();


            }
        }


    }

}