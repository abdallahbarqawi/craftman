package com.example.craftsman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
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

public class Register extends AppCompatActivity {

    EditText email, name, password, password2, phone;
    Button btnregister;
    ProgressBar loading;
    Spinner city;
    //url to connect android with php and server
    private static final String url = "https://aboodbarqawi.000webhostapp.com/craftman/register.php";

    // regular expression to check email if it in the form or not
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

    private static final Pattern USER_NAME = Pattern.compile("[a-zA-Z]{2,15}");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //view
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);
        btnregister = findViewById(R.id.btnregister);
        loading = findViewById(R.id.loading);
        phone = findViewById(R.id.phone);
        city= findViewById(R.id.city);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Register();
            }
        });
    }

    private void Register(){
        //add the visibility ta button and ProgressBar
        loading.setVisibility(View.VISIBLE);
        btnregister.setVisibility(View.GONE);
        //get text from input views
        final String email = this.email.getText().toString().trim().toLowerCase();
        final String name = this.name.getText().toString().trim();
        final String password = this.password.getText().toString().trim();
        final String password2 = this.password2.getText().toString().trim();
        final String phone = this.phone.getText().toString().trim();
        final String city = this.city.getSelectedItem().toString().trim();


        boolean checkemail = false;
        boolean checkname = false;
        boolean checkpassword1 = false;
        boolean checkpassword2 = false;
        boolean checkphone = false;
        boolean checkcity = false;
        //check the email if it correct or not
        if(email.isEmpty()){
            this.email.setError("Email can't be Empty");
        } else if(!EMAIL_ADDRESS.matcher(email).matches()){
            this.email.setError("Please inter A valid Email Address " +
                    "it should be like Example@gmail.com");
        }else{
            this.email.setError(null);
            checkemail = true;
        }
        //check the name if it not empty
        if(name.isEmpty()){
            this.name.setError("Name can't be Empty");
        } else if(!USER_NAME.matcher(name).matches()){
            this.name.setError("Name is Wrong format it should be Characters without numbers ");
        }else{
            this.name.setError(null);
            checkname = true;
        }

        //check the phone if it not empty
        if(phone.isEmpty()){
            this.phone.setError("Phone can't be Empty");
        } else if(phone.length() != 10  ){
            this.phone.setError("Phone too short");
        }else{
            this.phone.setError(null);
            checkphone = true;
        }
        // check the password if it the same or not
        if(city.equals("Select City"))
        {
//            Toast.makeText(this,"Please select your city ",Toast.LENGTH_SHORT).show();
            Toasty.error(this,"Please select your city ", Toasty.LENGTH_LONG,true).show();
        } else{
            checkcity = true;
        }
        // check the password if it is strong
        if(password.isEmpty()){
            this.password.setError("Password can't be Empty");
        } else if(!PASSWORD_PATTERN.matcher(password).matches()){
            this.password.setError("Your Password Is too weak" +
                    "please put  character(lower and upper) , numbers and  special character " +
                    "and at least 8 digit");
        }else{
            this.password.setError(null);
            checkpassword1 = true;
        }
        // check the password if it the same or not
        if(password2.isEmpty())
        {
            this.password2.setError("Password can't be Empty");
        }
        else if(!password2.equals(password))
        {
            this.password2.setError("The password not the Same");
        }else{
            this.password2.setError(null);
            checkpassword2 = true;
        }


        if(checkemail && checkname  && checkpassword1 && checkpassword2 && checkphone && checkcity)
        {
            // start the connection with the server
            // the request method post is the transfer for data from android to  php
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
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
//                            Toast.makeText(Register.this,"Register Success!",Toast.LENGTH_SHORT).show();
                            Toasty.success(Register.this,"Register Success!", Toasty.LENGTH_LONG,true).show();

                            Intent intent  = new Intent(Register.this,Login.class);

                            startActivity(intent);
                        }
                        else if(success.equals("2"))
                        {
//                            Toast.makeText(Register.this,"The Email is Exist Please change your Email ",Toast.LENGTH_SHORT).show();
                            Toasty.warning(Register.this,"The Email is Exist Please Login In with Your Email ", Toasty.LENGTH_LONG,true).show();
                            Intent intent  = new Intent(Register.this,Login.class);
                            startActivity(intent);

                        }
                        else if(success.equals("0"))
                        {
//                            Toast.makeText(Register.this,"Register Failed! ",Toast.LENGTH_SHORT).show();
                            Toasty.error(Register.this,"Register Failed! ", Toasty.LENGTH_LONG,true).show();
                            loading.setVisibility(View.GONE);
                            btnregister.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //if error occur
//                        Toast.makeText(Register.this,"Register Error!"+ e.toString(),Toast.LENGTH_SHORT).show();
                        Toasty.error(Register.this,"Register Error!"+ e.toString(), Toasty.LENGTH_LONG,true).show();

                        loading.setVisibility(View.GONE);
                        btnregister.setVisibility(View.VISIBLE);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(Register.this,"Register Error!"+ error.toString(),Toast.LENGTH_SHORT).show();
                    Toasty.error(Register.this,"Register Error!"+ error.toString(), Toasty.LENGTH_LONG,true).show();


                    loading.setVisibility(View.GONE);
                    btnregister.setVisibility(View.VISIBLE);
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //passing data to server
                    //with params we can put data that will passing with key to identify each one to php code
                    Map<String, String> params = new HashMap<>();
                    params.put("name",name);
                    params.put("email",email);
                    params.put("password",password);
                    params.put("phone",phone);
                    params.put("address",city);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
        else{
            loading.setVisibility(View.GONE);
            btnregister.setVisibility(View.VISIBLE);
//            Toast.makeText(Register.this,"Please Check your Entries",Toast.LENGTH_SHORT).show();
            Toasty.error(Register.this,"Please Check your Entries", Toasty.LENGTH_LONG,true).show();


        }


    }
}