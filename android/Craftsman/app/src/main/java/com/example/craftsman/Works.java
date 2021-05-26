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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class Works extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView mRecyclerView ;
    WorkMyAdapter myAdapter;
    ArrayList<WorkModel> workList;
    private static final String WORK_URL = "https://aboodbarqawi.000webhostapp.com/craftman/getWork.php";


    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    ActionBarDrawerToggle mToggle;
    SessionManager sessionManager;
    String getId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_works);
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

        workList = new ArrayList<>();

        Intent i = getIntent();
        String category = i.getStringExtra("category");


        // get user data from session
        HashMap<String, String> user = sessionManager.getUserDetail();

        getId = user.get(SessionManager.ID);

        loadWork(category);
    }
    public void loadWork(String category){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WORK_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray service = new JSONArray(response);
                            for(int i= 0; i<service.length();i++){
                                JSONObject serviceObject  = service.getJSONObject(i);
                                String title = serviceObject.getString("title");
                                String price = serviceObject.getString("price");
                                String image = serviceObject.getString("image");
                                String category_name = serviceObject.getString("name");
                                String phone = serviceObject.getString("phone");
                                String address = serviceObject.getString("address");
                                String certificate = serviceObject.getString("certificate");
                                String id = serviceObject.getString("id");
                                String description = serviceObject.getString("description");
                                String image_Url = "https://aboodbarqawi.000webhostapp.com/craftman/Images/"+image;
                                String certificate_Url = "https://aboodbarqawi.000webhostapp.com/craftman/Images/"+certificate;
                                WorkModel p = new WorkModel();
                                p.setId(id);
                                p.setTitle(title);
                                p.setPrice(price);
                                p.setWorkImg(image_Url);
                                p.setCategory(category_name);
                                p.setCall(phone);
                                p.setAddress(address);
                                p.setCertificate(certificate_Url);
                                p.setDescription(description);
                                workList.add(p);
                            }
                            myAdapter = new WorkMyAdapter(Works.this, workList);
                            mRecyclerView.setAdapter(myAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(Works.this, error.getMessage(),Toast.LENGTH_SHORT).show();
                Toasty.error(Works.this,error.getMessage(), Toasty.LENGTH_LONG,true).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //passing data to server
                //with params we can put data that will passing with key to identify each one to php code
                Map<String, String> params = new HashMap<>();
                params.put("category",category);
                return params;
            }
        };

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
            case R.id.home:
                intent =  new Intent(Works.this,Categories.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.list:
//                intent =  new Intent(Categories.this,CustomerList.class);
//                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.my_profile:
                intent =  new Intent(Works.this,Profile.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.logout:
                sessionManager.logout();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.add_post:
                intent =  new Intent(Works.this,AddPost.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

}