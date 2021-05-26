package com.example.craftsman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

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

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class Reports extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView mRecyclerView ;
    ReportMyAdapter myAdapter;
    ArrayList<ReportModel> reportList;
    private static final String REPORT_URL = "https://aboodbarqawi.000webhostapp.com/craftman/getReport.php";


    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    ActionBarDrawerToggle mToggle;
    SessionManager sessionManager;
    String getId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

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
        //recycler view
        mRecyclerView = findViewById(R.id.recycler_view);
        //set it's properties
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//linear layout
        //mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));//grid layout here 2  means 2 columns in each row

        reportList = new ArrayList<>();

        loadReports();
    }

    public void loadReports(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, REPORT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray service = new JSONArray(response);
                            for(int i= 0; i<service.length();i++){
                                JSONObject serviceObject  = service.getJSONObject(i);
                                String id = serviceObject.getString("id");
                                String report = serviceObject.getString("report");
                                String type = serviceObject.getString("type");
                                String name = serviceObject.getString("name");
                                String typeName = "";
                                if(type.equals("0")){
                                    typeName = "Service";
                                }else if(type.equals("1")){
                                    typeName = "Work";
                                }
                                ReportModel p = new ReportModel();
                                p.setReportId(id);
                                p.setReport(report);
                                p.setReportType(typeName);
                                p.setUsername(name);
                                reportList.add(p);
                            }
                            myAdapter = new ReportMyAdapter(Reports.this, reportList);
                            mRecyclerView.setAdapter(myAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(Reports.this, error.getMessage(),Toast.LENGTH_SHORT).show();
                Toasty.error(Reports.this, error.getMessage(), Toasty.LENGTH_LONG,true).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //called when click search
                myAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //called whenever type each letter in search bar
                myAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_search){
            return true;
        }
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
                intent =  new Intent(Reports.this,MainActivity.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.users:
                intent =  new Intent(Reports.this,Users.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.report:
                intent =  new Intent(Reports.this,Reports.class);
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