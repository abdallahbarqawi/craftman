package com.example.craftsman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class Login extends AppCompatActivity {
    EditText ed_email , ed_password;
    Button login_btn, register_btn;
    ProgressBar loading;
    TextView forget;
    SessionManager sessionManager;
    //url to connect with the server
    private static String url = "https://aboodbarqawi.000webhostapp.com/craftman/login.php";


    //regular expression to check email if it in the right form  or not
    private static final Pattern EMAIL_ADDRESS = Pattern.compile("" +
            "[a-zA-Z0-9\\+\\.\\_\\-\\%\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})");
    // regular expression to check password if it strong or not
    private static final Pattern PASSWORD_PATTERN= Pattern.compile("^" +
            "(?=.*[0-9])" + //at least one digit
            "(?=.*[a-z])" + //at least one lower case
            "(?=.*[A-Z])" + //at least one upper case
            "(?=.*[@#$%^&+=])" + //at least one special character
            "(?=\\S+$)" + //No white space
            ".{8,}" +     //at least 8 character
            "$");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // get session this / login activity
        sessionManager =  new SessionManager(getApplicationContext());

        if(sessionManager.islogin()) {
            HashMap<String, String> user = sessionManager.getUserDetail();

            String type = user.get(SessionManager.TYPE);
            if(type.equals("1")){
                Intent intent  = new Intent(Login.this,Categories.class);
                startActivity(intent);
            }
            else if(type.equals("0")){
                Intent intent  = new Intent(Login.this,MainActivity.class);
                startActivity(intent);
            }

        }


        register_btn = findViewById(R.id.register);
        login_btn = findViewById(R.id.btnlogin);
        ed_email = findViewById(R.id.email);
        ed_password = findViewById(R.id.password);
        loading = findViewById(R.id.loading1);
        forget = findViewById(R.id.forget);



        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Login.this, Register.class);

                startActivity(i);
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, OtpSender.class);

                startActivity(i);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //variables to store the input from the edit text
                String ed = ed_email.getText().toString().trim().toLowerCase();
                String pass = ed_password.getText().toString().trim();
                boolean check1 = false;
                boolean check2 = false;

                // check the input if it is not empty and it in the right form
                if(ed.isEmpty()){
                    ed_email.setError("Email can't be Empty");
                } else if(!EMAIL_ADDRESS.matcher(ed).matches()){
                    ed_email.setError("Please enter A valid Email Address " +
                            "it should be like Example@gmail.com");
                }else{
                    ed_email.setError(null);
                    check1 = true;
                }
                if(pass.isEmpty()){
                    ed_password.setError("Password can't be Empty");
                } else if(!PASSWORD_PATTERN.matcher(pass).matches()){
                    ed_password.setError("Your Password Is too weak " +
                            "should contain characters(lower and upper) , numbers and  special character " +
                            "and at least 8 digit");
                }else{
                    ed_password.setError(null);
                    check2 = true;
                }
                if(check1 && check2) {
                    login(ed, pass);
                }
            }
        });

    }


    public void login(final String email, final String password){
        loading.setVisibility(View.VISIBLE);
        login_btn.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if(success.equals("1"))
                            {
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id =object.getString("id").trim();
                                    String name =object.getString("name").trim();
                                    String email =object.getString("email").trim();
                                    String user_type =object.getString("type").trim();

                                    sessionManager.createSession(name,email,id,user_type);


                                    if(user_type.equals("1")){
                                        Intent intent  = new Intent(Login.this,Categories.class);
                                        startActivity(intent);
                                    }
                                    else if(user_type.equals("0")){
                                        Intent intent  = new Intent(Login.this,MainActivity.class);
                                        startActivity(intent);
                                    }

                                    loading.setVisibility(View.GONE);
                                }
                            }
                            else if(success.equals("0")){
//                                Toast.makeText(Login.this,"Your Email or Password is wrong",Toast.LENGTH_SHORT).show();
                                Toasty.error(Login.this,"Your Email or Password is wrong", Toasty.LENGTH_LONG,true).show();
                                loading.setVisibility(View.GONE);
                                login_btn.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            login_btn.setVisibility(View.VISIBLE);
//                            Toast.makeText(Login.this," Error! "+ e.toString(),Toast.LENGTH_SHORT).show();
                            Toasty.error(Login.this," Error! "+ e.toString(), Toasty.LENGTH_LONG,true).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
                login_btn.setVisibility(View.VISIBLE);
//                Toast.makeText(Login.this," Error! "+ error.toString(),Toast.LENGTH_SHORT).show();
                Toasty.error(Login.this," Error! "+ error.toString(), Toasty.LENGTH_LONG,true).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //passing data to server
                //with params we can put data that will passing with key to identify each one

                Map<String, String> params = new HashMap<>();
                params.put("email",email);
                params.put("password",password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}