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
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ReportDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    TextView id, name, report, type;
    Button delete_btn;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    ActionBarDrawerToggle mToggle;
    SessionManager sessionManager;
    String getId;

    ProgressBar loading_delete;

    private static final String DELETE_REPORT_URL = "https://aboodbarqawi.000webhostapp.com/craftman/deleteReport.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);

        id = findViewById(R.id.detail_id);
        name = findViewById(R.id.report_username);
        report = findViewById(R.id.report_description);
        type = findViewById(R.id.report_type);
        delete_btn = findViewById(R.id.delete_report);
        loading_delete = findViewById(R.id.loading);



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


        Intent i = getIntent();
        String str_id = i.getStringExtra("id");
        String str_name = i.getStringExtra("name");
        String str_report = i.getStringExtra("report");
        String str_type = i.getStringExtra("type");

        id.setText(str_id);
        name.setText(str_name);
        report.setText(str_report);
        type.setText(str_type);

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteReport(str_id);
            }
        });


    }

    public void deleteReport(String id){
        loading_delete.setVisibility(View.VISIBLE);
        delete_btn.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELETE_REPORT_URL,
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
//                                Toast.makeText(ReportDetails.this,"Delete Report Success!",Toast.LENGTH_SHORT).show();
                                Toasty.success(ReportDetails.this,"Delete Report Success!", Toasty.LENGTH_LONG,true).show();
                                Intent intent  = new Intent(ReportDetails.this,Reports.class);

                                startActivity(intent);
                            }
                            else if(success.equals("0"))
                            {
//                                Toast.makeText(ReportDetails.this,"Delete Report Failed! ",Toast.LENGTH_SHORT).show();
                                Toasty.error(ReportDetails.this,"Delete Report Failed! ", Toasty.LENGTH_LONG,true).show();

                                loading_delete.setVisibility(View.GONE);
                                delete_btn.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //if error occur
//                            Toast.makeText(ReportDetails.this,"Delete Report Error!"+ e.toString(),Toast.LENGTH_SHORT).show();
                            Toasty.error(ReportDetails.this,"Delete Report Error!"+ e.toString(), Toasty.LENGTH_LONG,true).show();
                            loading_delete.setVisibility(View.GONE);
                            delete_btn.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(ReportDetails.this,"Register Error!"+ error.toString(),Toast.LENGTH_SHORT).show();
                Toasty.error(ReportDetails.this,"Delete Report Error!"+ error.toString(), Toasty.LENGTH_LONG,true).show();
                loading_delete.setVisibility(View.GONE);
                delete_btn.setVisibility(View.VISIBLE);
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
                intent =  new Intent(ReportDetails.this,MainActivity.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.users:
                intent =  new Intent(ReportDetails.this,Users.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.report:
                intent =  new Intent(ReportDetails.this,Reports.class);
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