package com.example.craftsman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class UserDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    ActionBarDrawerToggle mToggle;
    SessionManager sessionManager;

    TextView username, id , email, phone, state, address;
    Button btn_suspend, btn_active;
    ProgressBar loading;

    private static final String SUSPEND_URL = "https://aboodbarqawi.000webhostapp.com/craftman/suspend.php";
    private static final String ACTIVE_URL = "https://aboodbarqawi.000webhostapp.com/craftman/active.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        sessionManager =  new SessionManager(this);
        sessionManager.checkLogin();




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

        username = findViewById(R.id.detail_user_name);
        id = findViewById(R.id.detail_user_id);
        address = findViewById(R.id.detail_user_address);
        email = findViewById(R.id.detail_user_email);
        phone = findViewById(R.id.detail_user_phone);
        state = findViewById(R.id.detail_user_state);
        btn_suspend = findViewById(R.id.suspend);
        btn_active = findViewById(R.id.active);
        loading = findViewById(R.id.loading);

        id.setVisibility(View.GONE);


        Intent i = getIntent();
        String str_id = i.getStringExtra("id");
        String str_name = i.getStringExtra("name");
        String str_email = i.getStringExtra("email");
        String str_address = i.getStringExtra("address");
        String str_state = i.getStringExtra("state");
        String str_phone = i.getStringExtra("phone");

        id.setText(str_id);
        username.setText(str_name);
        address.setText(str_address);
        email.setText(str_email);
        phone.setText(str_phone);
        if(str_state.equals("0")){
            state.setText("Suspend");
            btn_suspend.setVisibility(View.GONE);
            btn_active.setVisibility(View.VISIBLE);
        }else if(str_state.equals("1")){
            state.setText("Activated");
            btn_suspend.setVisibility(View.VISIBLE);
            btn_active.setVisibility(View.GONE);
        }

        btn_suspend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suspend(str_id);
            }
        });
        btn_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                active(str_id);
            }
        });

    }

    public void suspend(String id){
        loading.setVisibility(View.VISIBLE);
        btn_suspend.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SUSPEND_URL,
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
//                                Toast.makeText(UserDetails.this,"Suspend Account Success!",Toast.LENGTH_SHORT).show();
                                Toasty.success(UserDetails.this, "Suspend Account Success!", Toasty.LENGTH_LONG,true).show();
                                Intent intent  = new Intent(UserDetails.this,Users.class);
                                startActivity(intent);
                            }
                            else if(success.equals("0"))
                            {
//                                Toast.makeText(UserDetails.this,"Suspend Account Failed! ",Toast.LENGTH_SHORT).show();
                                Toasty.error(UserDetails.this, "Suspend Account Failed! ", Toasty.LENGTH_LONG,true).show();
                                loading.setVisibility(View.GONE);
                                btn_suspend.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //if error occur
//                            Toast.makeText(UserDetails.this,"Suspend Account Error!"+ e.toString(),Toast.LENGTH_SHORT).show();
                            Toasty.error(UserDetails.this, "Suspend Account Error! ", Toasty.LENGTH_LONG,true).show();


                            loading.setVisibility(View.GONE);
                            btn_suspend.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(UserDetails.this,"Suspend Account Error!"+ error.toString(),Toast.LENGTH_SHORT).show();
                Toasty.error(UserDetails.this, "Suspend Account Error!"+ error.toString(), Toasty.LENGTH_LONG,true).show();
                loading.setVisibility(View.GONE);
                btn_suspend.setVisibility(View.VISIBLE);
            }
        })
        {
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

    public void active(String id){
        loading.setVisibility(View.VISIBLE);
        btn_active.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ACTIVE_URL,
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
//                                Toast.makeText(UserDetails.this,"Activated Account  Success!",Toast.LENGTH_SHORT).show();
                                Toasty.success(UserDetails.this,"Activated Account  Success!", Toasty.LENGTH_LONG,true).show();
                                Intent intent  = new Intent(UserDetails.this,Users.class);

                                startActivity(intent);
                            }
                            else if(success.equals("0"))
                            {
//                                Toast.makeText(UserDetails.this,"Activated Account Failed! ",Toast.LENGTH_SHORT).show();
                                Toasty.error(UserDetails.this,"Activated Account Failed! ", Toasty.LENGTH_LONG,true).show();

                                loading.setVisibility(View.GONE);
                                btn_active.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //if error occur
//                            Toast.makeText(UserDetails.this,"Activated Account Error!"+ e.toString(),Toast.LENGTH_SHORT).show();
                            Toasty.error(UserDetails.this,"Activated Account Error!"+ e.toString(), Toasty.LENGTH_LONG,true).show();
                            loading.setVisibility(View.GONE);
                            btn_active.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(UserDetails.this,"Activated Account Error!"+ error.toString(),Toast.LENGTH_SHORT).show();
                Toasty.error(UserDetails.this,"Activated Account Error!"+ error.toString(), Toasty.LENGTH_LONG,true).show();

                loading.setVisibility(View.GONE);
                btn_active.setVisibility(View.VISIBLE);
            }
        })
        {
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
            case R.id.my_home:
                intent =  new Intent(UserDetails.this,MainActivity.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.users:
                intent =  new Intent(UserDetails.this,Users.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.report:
                intent =  new Intent(UserDetails.this,Reports.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.logout:
                sessionManager.logoutAdmin();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

        }
        return true;
    }
}