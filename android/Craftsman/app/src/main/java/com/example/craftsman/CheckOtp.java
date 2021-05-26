package com.example.craftsman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class CheckOtp extends AppCompatActivity {

    Button btn_check;
    EditText otp_number;
    String code ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_otp);

        btn_check = findViewById(R.id.btncheck);
        otp_number = findViewById(R.id.otpnumber);

        Intent i = getIntent();
        code = i.getStringExtra("code");

        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check  = otp_number.getText().toString().trim();

                if(code.equals(check)){
                    Intent i = new Intent(CheckOtp.this, ForgotPassword.class);
                    startActivity(i);

                }else{
//                    Toast.makeText(CheckOtp.this, "InValid Otp", Toast.LENGTH_LONG).show();
                    Toasty.error(CheckOtp.this,"InValid Otp",Toast.LENGTH_SHORT,true).show();
                    Intent i = new Intent(CheckOtp.this, OtpSender.class);
                    startActivity(i);
                }
            }
        });
    }
}