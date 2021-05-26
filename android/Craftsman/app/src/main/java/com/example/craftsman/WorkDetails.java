package com.example.craftsman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class WorkDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    TextView id, title, price, address, category, description, phone;
    Button call_btn, save_comment_btn,save_report_btn;
    ImageView img, certificate;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    ActionBarDrawerToggle mToggle;
    SessionManager sessionManager;
    String str_phone,getId;
    EditText comment_ed,report_ed;
    ProgressBar loading_comment,loading_report;

    RecyclerView mRecyclerView ;
    CommentMyAdapter myAdapter;
    ArrayList<CommentModel> commentList;


    private static final int REQUEST_CALL = 1;

    private static final String GET_COMMENT_URL = "https://aboodbarqawi.000webhostapp.com/craftman/getWorkComment.php";
    private static final String POST_COMMENT_URL = "https://aboodbarqawi.000webhostapp.com/craftman/saveWorkComment.php";
    private static final String GET_Rating_URL = "https://aboodbarqawi.000webhostapp.com/craftman/getWorkRating.php";
    private static final String POST_Rating_URL = "https://aboodbarqawi.000webhostapp.com/craftman/saveWorkRating.php";
    private static final String POST_Report_URL = "https://aboodbarqawi.000webhostapp.com/craftman/saveServiceReport.php";




    private static final Pattern COMMENT_PATTERN = Pattern.compile("[a-zA-Z]{10,500}");

    RatingBar ratingBar;
    float rating = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_details);
        id = findViewById(R.id.detail_id);
        title = findViewById(R.id.detail_title);
        price = findViewById(R.id.detail_price);
        address = findViewById(R.id.detail_address);
        category = findViewById(R.id.detail_category);
        description = findViewById(R.id.detail_description);
        phone = findViewById(R.id.detail_phone);
        img = findViewById(R.id.detail_img);
        certificate = findViewById(R.id.detail_certificate);
        call_btn = findViewById(R.id.call);
        save_comment_btn = findViewById(R.id.save_comment_btn);
        comment_ed = findViewById(R.id.comment_ed);
        report_ed = findViewById(R.id.report_ed);
        loading_comment = findViewById(R.id.loading_comment);
        loading_report = findViewById(R.id.loading_report);
        save_report_btn = findViewById(R.id.save_report_btn);
        ratingBar = findViewById(R.id.ratring);

        id.setVisibility(View.GONE);

        //defined drawer for activity
        mDrawerLayout = findViewById(R.id.drawer);
        // add top side button to control on drawer
        mToggle =  new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        //add mToggle to DrawerLayout
        mDrawerLayout.addDrawerListener(mToggle);
        // take mtoggle state
        //It will synchronized the icon from the drawer and the drawer itself where when you move the drawer the icon rotate
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNavigationView = findViewById(R.id.navigation);

        mNavigationView.setNavigationItemSelectedListener(this);
        sessionManager =  new SessionManager(this);
        sessionManager.checkLogin();
        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetail();

        getId = user.get(SessionManager.ID);



        //recycler view
        mRecyclerView = findViewById(R.id.recycler_view);
        //set it's properties
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//linear layout
        //mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));//grid layout here 2  means 2 columns in each row

        commentList = new ArrayList<>();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = ratingBar.getRating();
            }
        });



        Intent i = getIntent();
        String str_id = i.getStringExtra("id");
        String str_title = i.getStringExtra("title");
        String str_category = i.getStringExtra("category");
        String str_address = i.getStringExtra("address");
        String str_description = i.getStringExtra("description");
        String str_image = i.getStringExtra("image");
        String str_certificate = i.getStringExtra("certificate");
        str_phone = i.getStringExtra("phone");
        String str_price = i.getStringExtra("price");

        title.setText("Title : "+str_title);
        address.setText("Address : "+str_address);
        description.setText(str_description);
        category.setText(str_category);
        phone.setText(str_phone);
        price.setText(str_price);
        Picasso.with(WorkDetails.this).load(str_image).into(img);
        Picasso.with(WorkDetails.this).load(str_certificate).into(certificate);

        loadComments(str_id);
        loadRating(str_id);
        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePhoneCall(str_phone);
            }
        });

        save_comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRating(str_id, rating);
                saveComment(str_id);
            }
        });

        save_report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveReport(str_id,getId);
            }
        });


    }
    public void makePhoneCall(String phone){
        if(ContextCompat.checkSelfPermission(WorkDetails.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(WorkDetails.this, new String[]{Manifest.permission.CALL_PHONE},1);
        }else{
            String dial = "tel:" + phone;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

// get all comment from database
    public void loadComments(String id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_COMMENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray service = new JSONArray(response);
                            for(int i= 0; i<service.length();i++){
                                JSONObject serviceObject  = service.getJSONObject(i);
                                String comment = serviceObject.getString("comment");
                                CommentModel p = new CommentModel();
                                p.setComment(comment);
                                commentList.add(p);
                            }
                            myAdapter = new CommentMyAdapter(WorkDetails.this, commentList);
                            mRecyclerView.setAdapter(myAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(WorkDetails.this, error.getMessage(),Toast.LENGTH_SHORT).show();
                Toasty.error(WorkDetails.this, error.getMessage(), Toasty.LENGTH_LONG,true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //passing data to server
                //with params we can put data that will passing with key to identify each one to php code
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void loadRating(String id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_Rating_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray service = new JSONArray(response);
                            for(int i= 0; i<service.length();i++){
                                JSONObject serviceObject  = service.getJSONObject(i);
                                String rating = serviceObject.getString("rating");
                                ratingBar.setRating(Float.parseFloat(rating));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(WorkDetails.this, error.getMessage(),Toast.LENGTH_SHORT).show();
                Toasty.error(WorkDetails.this, error.getMessage(), Toasty.LENGTH_LONG,true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //passing data to server
                //with params we can put data that will passing with key to identify each one to php code
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void saveComment(String id){
        //add the visibility ta button and ProgressBar

        loading_comment.setVisibility(View.VISIBLE);
        save_comment_btn.setVisibility(View.GONE);
        //get text from input views
        final String comment = this.comment_ed.getText().toString().trim();



        boolean checkcomment = false;

        //check the email if it correct or not
        if(comment.isEmpty()){
            this.comment_ed.setError("Comment can't be Empty");
        } else if(comment.length()<10){
            this.comment_ed.setError("Comment is Wrong format it should be Characters without numbers");
        }else{
            this.comment_ed.setError(null);
            checkcomment = true;
        }


        if(checkcomment)
        {
            // start the connection with the server
            // the request method post is the transfer for data from android to  php
            StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_COMMENT_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                //to get the response when we pass data to the server
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                // if the data store in database will execute if and enter to home page
                                if(success.equals("1"))
                                {
//                                    Toast.makeText(WorkDetails.this,"Add Comment Success!",Toast.LENGTH_SHORT).show();
                                    Toasty.success(WorkDetails.this, "Add Comment Success!", Toasty.LENGTH_LONG,true).show();
                                    Intent intent  = new Intent(WorkDetails.this,Categories.class);

                                    startActivity(intent);
                                }
                                else if(success.equals("0"))
                                {
//                                    Toast.makeText(WorkDetails.this,"Add Comment Failed! ",Toast.LENGTH_SHORT).show();
                                    Toasty.error(WorkDetails.this, "Add Comment Failed! ", Toasty.LENGTH_LONG,true).show();

                                    loading_comment.setVisibility(View.GONE);
                                    save_comment_btn.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                //if error occur
//                                Toast.makeText(WorkDetails.this,"Add Comment Error!"+ e.toString(),Toast.LENGTH_SHORT).show();
                                Toasty.error(WorkDetails.this, "Add Comment Error!"+ e.toString(), Toasty.LENGTH_LONG,true).show();
                                loading_comment.setVisibility(View.GONE);
                                save_comment_btn.setVisibility(View.VISIBLE);
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(WorkDetails.this,"Register Error!"+ error.toString(),Toast.LENGTH_SHORT).show();
                    Toasty.error(WorkDetails.this, "Add Comment Error!"+ error.toString(), Toasty.LENGTH_LONG,true).show();
                    loading_comment.setVisibility(View.GONE);
                    save_comment_btn.setVisibility(View.VISIBLE);
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //passing data to server
                    //with params we can put data that will passing with key to identify each one to php code
                    Map<String, String> params = new HashMap<>();
                    params.put("id",id);
                    params.put("comment",comment);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
        else{
            loading_comment.setVisibility(View.GONE);
            save_comment_btn.setVisibility(View.VISIBLE);
//            Toast.makeText(WorkDetails.this,"Please Check your Entries",Toast.LENGTH_SHORT).show();
            Toasty.error(WorkDetails.this, "Please Check your Entries", Toasty.LENGTH_LONG,true).show();

        }
    }

    public void saveRating(String id, float rating){
        //add the visibility ta button and ProgressBar

        loading_comment.setVisibility(View.VISIBLE);
        save_comment_btn.setVisibility(View.GONE);
        boolean checkcomment = false;
        if(rating == 0){
            checkcomment = true;
        }else{
            checkcomment = true;
        }

        if(checkcomment)
        {
            // start the connection with the server
            // the request method post is the transfer for data from android to  php
            StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_Rating_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                //to get the response when we pass data to the server
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                // if the data store in database will execute if and enter to home page
                                if(success.equals("1"))
                                {
//                                    Toast.makeText(WorkDetails.this,"Add rating Success!",Toast.LENGTH_SHORT).show();
                                    Toasty.success(WorkDetails.this, "Add rating Success!", Toasty.LENGTH_LONG,true).show();


                                }
                                else if(success.equals("0"))
                                {
//                                    Toast.makeText(WorkDetails.this,"Add rating Failed! ",Toast.LENGTH_SHORT).show();
                                    Toasty.error(WorkDetails.this, "Add rating Failed! ", Toasty.LENGTH_LONG,true).show();
                                    loading_comment.setVisibility(View.GONE);
                                    save_comment_btn.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                //if error occur
//                                Toast.makeText(WorkDetails.this,"Add rating Error!"+ e.toString(),Toast.LENGTH_SHORT).show();
                                Toasty.error(WorkDetails.this, "Add rating Error!"+ e.toString(), Toasty.LENGTH_LONG,true).show();

                                loading_comment.setVisibility(View.GONE);
                                save_comment_btn.setVisibility(View.VISIBLE);
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(WorkDetails.this,"Add rating Error!"+ error.toString(),Toast.LENGTH_SHORT).show();
                    Toasty.error(WorkDetails.this, "Add rating Error!"+ error.toString(), Toasty.LENGTH_LONG,true).show();
                    loading_comment.setVisibility(View.GONE);
                    save_comment_btn.setVisibility(View.VISIBLE);
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //passing data to server
                    //with params we can put data that will passing with key to identify each one to php code
                    Map<String, String> params = new HashMap<>();
                    params.put("id",id);
                    params.put("rating",rating+"");
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
        else{
            loading_comment.setVisibility(View.GONE);
            save_comment_btn.setVisibility(View.VISIBLE);
//            Toast.makeText(WorkDetails.this,"Please Check your Entries",Toast.LENGTH_SHORT).show();
            Toasty.error(WorkDetails.this, "Please Check your Entries", Toasty.LENGTH_LONG,true).show();

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCall(str_phone);
            }else{
//                Toast.makeText(WorkDetails.this, "Permission Denied ", Toast.LENGTH_LONG).show();
                Toasty.error(WorkDetails.this, "Permission Denied ", Toasty.LENGTH_LONG,true).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;

        switch(item.getItemId()){
            case R.id.home:
                intent =  new Intent(WorkDetails.this,Categories.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.list:
//                intent =  new Intent(Categories.this,CustomerList.class);
//                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.my_profile:
                intent =  new Intent(WorkDetails.this,Profile.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.logout:
                sessionManager.logout();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.add_post:
                intent =  new Intent(WorkDetails.this,AddPost.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    public void saveReport(String service_id, String user_id){
        //add the visibility ta button and ProgressBar

        loading_report.setVisibility(View.VISIBLE);
        save_report_btn.setVisibility(View.GONE);
        //get text from input views
        final String report = this.report_ed.getText().toString().trim();



        boolean checkcomment = false;

        //check the email if it correct or not
        if(report.isEmpty()){
            this.report_ed.setError("Report can't be Empty");
        } else if(report.length()<10){
            this.report_ed.setError("Report is Wrong format it should be Characters without numbers");
        }else{
            this.report_ed.setError(null);
            checkcomment = true;
        }


        if(checkcomment)
        {
            // start the connection with the server
            // the request method post is the transfer for data from android to  php
            StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_Report_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                //to get the response when we pass data to the server
                                JSONObject jsonObject = new JSONObject(response);
                                String success = jsonObject.getString("success");
                                // if the data store in database will execute if and enter to home page
                                if(success.equals("1"))
                                {
//                                    Toast.makeText(WorkDetails.this,"Send Report Success!",Toast.LENGTH_SHORT).show();
                                    Toasty.success(WorkDetails.this, "Send Report Success!", Toasty.LENGTH_LONG,true).show();
                                    Intent intent  = new Intent(WorkDetails.this,Categories.class);

                                    startActivity(intent);
                                }
                                else if(success.equals("0"))
                                {
                                    //Toast.makeText(WorkDetails.this,"Send Report Failed! ",Toast.LENGTH_SHORT).show();
                                    Toasty.error(WorkDetails.this, "Send Report Failed! ", Toasty.LENGTH_LONG,true).show();
                                    loading_report.setVisibility(View.GONE);
                                    save_report_btn.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                //if error occur
//                                Toast.makeText(WorkDetails.this,"Send Report Error!"+ e.toString(),Toast.LENGTH_SHORT).show();
                                Toasty.error(WorkDetails.this, "Send Report Error!"+ e.toString(), Toasty.LENGTH_LONG,true).show();

                                loading_report.setVisibility(View.GONE);
                                save_report_btn.setVisibility(View.VISIBLE);
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(WorkDetails.this,"Send Report Error!"+ error.toString(),Toast.LENGTH_SHORT).show();
                    Toasty.error(WorkDetails.this,"Send Report Error!"+ error.toString(), Toasty.LENGTH_LONG,true).show();

                    loading_report.setVisibility(View.GONE);
                    save_report_btn.setVisibility(View.VISIBLE);
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //passing data to server
                    //with params we can put data that will passing with key to identify each one to php code
                    Map<String, String> params = new HashMap<>();
                    params.put("id",user_id);
                    params.put("service_id",service_id);
                    params.put("report",report);
                    params.put("type","1");
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
        else{
            loading_report.setVisibility(View.GONE);
            save_report_btn.setVisibility(View.VISIBLE);
//            Toast.makeText(WorkDetails.this,"Please Check your Entries",Toast.LENGTH_SHORT).show();
            Toasty.error(WorkDetails.this,"Please Check your Entries", Toasty.LENGTH_LONG,true).show();

        }
    }

}