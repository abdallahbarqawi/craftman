package com.example.craftsman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class ForgotPassword extends AppCompatActivity {

    EditText ed_email , ed_password, ed_password2;
    Button submit_btn;
    ProgressBar loading;

    //url to connect with the server
    private static String url = "https://aboodbarqawi.000webhostapp.com/craftman/resetpassword.php";


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
        setContentView(R.layout.activity_forgot_password);

        ed_email = findViewById(R.id.email);
        ed_password = findViewById(R.id.password);
        ed_password2 = findViewById(R.id.password2);
        submit_btn = findViewById(R.id.btnsubmit);
        loading = findViewById(R.id.loading1);



        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //variables to store the input from the edit text
                String ed = ed_email.getText().toString().trim().toLowerCase();
                String pass = ed_password.getText().toString().trim();
                String pass2 = ed_password2.getText().toString().trim();
                boolean check1 = false;
                boolean check2 = false;
                boolean check3 = false;

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
                // check the password if it the same or not
                if(pass2.isEmpty())
                {
                    ed_password2.setError("Password can't be Empty");
                }
                else if(!pass2.equals(pass))
                {
                    ed_password2.setError("The password not the Same");
                }else{
                    ed_password2.setError(null);
                    check3 = true;
                }
                if(check1 && check2 && check3) {
                    reset(ed, pass);
                }
            }
        });
    }
    public void reset(final String email, final String password){
        loading.setVisibility(View.VISIBLE);
        submit_btn.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            // if the data store in database will execute if and enter to home page
                            if(success.equals("1"))
                            {
//                                Toast.makeText(ForgotPassword.this,"Reset Password Success!",Toast.LENGTH_SHORT).show();
                                Toasty.success(ForgotPassword.this,"Reset Password Success! ",Toast.LENGTH_SHORT,true).show();
                                Intent intent  = new Intent(ForgotPassword.this,Login.class);
                                startActivity(intent);
                            }
                            else if(success.equals("0")){
//                                Toast.makeText(ForgotPassword.this,"Your Email or Password is wrong",Toast.LENGTH_SHORT).show();
                                Toasty.error(ForgotPassword.this,"Reset Password Failed!",Toast.LENGTH_SHORT,true).show();

                                loading.setVisibility(View.GONE);
                                submit_btn.setVisibility(View.VISIBLE);
                            }
                            else if(success.equals("2")){
//                                Toast.makeText(ForgotPassword.this,"Your Email Does not Exist Please Create Account",Toast.LENGTH_SHORT).show();
                                Toasty.error(ForgotPassword.this,"Your Email Does not Exist Please Create Account",Toast.LENGTH_SHORT,true).show();

                                Intent intent  = new Intent(ForgotPassword.this,Login.class);
                                startActivity(intent);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            submit_btn.setVisibility(View.VISIBLE);
//                            Toast.makeText(ForgotPassword.this," Error! "+ e.toString(),Toast.LENGTH_SHORT).show();
                            Toasty.error(ForgotPassword.this," Error! "+ e.toString(),Toast.LENGTH_SHORT,true).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.GONE);
                submit_btn.setVisibility(View.VISIBLE);
//                Toast.makeText(ForgotPassword.this," Error! "+ error.toString(),Toast.LENGTH_SHORT).show();
                Toasty.error(ForgotPassword.this," Error! "+ error.toString(),Toast.LENGTH_SHORT,true).show();
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