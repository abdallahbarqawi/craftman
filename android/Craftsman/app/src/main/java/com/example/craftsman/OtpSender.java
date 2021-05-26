package com.example.craftsman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Random;

import es.dmoral.toasty.Toasty;

public class OtpSender extends AppCompatActivity {
    Button btn_otp;
    EditText phone;
    private static final String Phone_URL = "https://aboodbarqawi.000webhostapp.com/craftman/checkPhone.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_sender);

        phone = findViewById(R.id.phonenumber);
        btn_otp = findViewById(R.id.btnotp);

        btn_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED ){
                    if(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED ) {
                        sendSms();
                    }
                }else{
                    requestPermissions( new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE},1);
                }

            }
        });
    }
    public void sendSms(){
//        Toast.makeText(OtpSender.this, "Main Activity Send SMS", Toast.LENGTH_LONG).show();

        Random rand = new Random();
        String phone = this.phone.getText().toString().trim();
        boolean checkphone = false;
        if(phone.isEmpty()){
            this.phone.setError("Phone can't be Empty");
        } else if(phone.length() != 10  ){
            this.phone.setError("Phone too short");
        }else{
            this.phone.setError(null);
            checkphone = true;
        }
        if(checkphone){
//            String code = String.format("%04d", rand.nextInt(10000));
//            try{
//                SmsManager smsManager = SmsManager.getDefault();
//                smsManager.sendTextMessage(phone, null, "your OTp code is  "+code, null, null);
//                Toast.makeText(OtpSender.this, "Message Send", Toast.LENGTH_LONG).show();
//                Intent i = new Intent(OtpSender.this, CheckOtp.class);
//                i.putExtra("code", code);
//                startActivity(i);
//            }catch(Exception e){
//                e.printStackTrace();
//                Toast.makeText(OtpSender.this, "Failed Message Send   ***** "+ e, Toast.LENGTH_LONG).show();
//            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Phone_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        //to get the response when we pass data to the server
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        // if the data store in database will execute if and enter to home page
                        if(success.equals("1"))
                        {
                            String code = String.format("%04d", rand.nextInt(10000));
                            try{
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(phone, null, "your OTp code is  "+code, null, null);
//                                Toast.makeText(OtpSender.this, "Message Send", Toast.LENGTH_LONG).show();
                                Toasty.success(OtpSender.this,"Message Send", Toasty.LENGTH_LONG,true).show();

                                Intent i = new Intent(OtpSender.this, CheckOtp.class);
                                i.putExtra("code", code);
                                startActivity(i);
                            }catch(Exception e){
                                e.printStackTrace();
//                                Toast.makeText(OtpSender.this, "Failed Message Send "+ e, Toast.LENGTH_LONG).show();
                                Toasty.error(OtpSender.this,"Failed Message Send "+ e, Toasty.LENGTH_LONG,true).show();

                            }
                        }
                        else if(success.equals("0"))
                        {
//                            Toast.makeText(OtpSender.this,"Please Check Your Phone Number ! ",Toast.LENGTH_SHORT).show();
                            Toasty.error(OtpSender.this,"Please Check Your Phone Number ! ", Toasty.LENGTH_LONG,true).show();
                            btn_otp.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //if error occur
//                        Toast.makeText(OtpSender.this,"Failed Message Send !"+ e.toString(),Toast.LENGTH_SHORT).show();
                        Toasty.error(OtpSender.this,"Failed Message Send "+ e.toString(), Toasty.LENGTH_LONG,true).show();


                        btn_otp.setVisibility(View.VISIBLE);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    Toast.makeText(OtpSender.this,"Failed Message Send !"+ error.toString(),Toast.LENGTH_SHORT).show();
                    Toasty.error(OtpSender.this,"Failed Message Send "+ error.toString(), Toasty.LENGTH_LONG,true).show();
                    btn_otp.setVisibility(View.VISIBLE);
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //passing data to server
                    //with params we can put data that will passing with key to identify each one to php code
                    Map<String, String> params = new HashMap<>();
                    params.put("phone",phone);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1 && grantResults.length>0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED && grantResults[1] ==PackageManager.PERMISSION_GRANTED ){
            sendSms();
        }else{
//            Toast.makeText(OtpSender.this, "PERMISSION_Denied", Toast.LENGTH_LONG).show();
            Toasty.error(OtpSender.this,"PERMISSION Denied", Toasty.LENGTH_LONG,true).show();
        }
    }


}