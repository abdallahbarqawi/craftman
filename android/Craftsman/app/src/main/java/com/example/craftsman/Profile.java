package com.example.craftsman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String CUSTOMER_URL = "https://aboodbarqawi.000webhostapp.com/craftman/getuserdata.php";
    private static final String URL_EDIT = "https://aboodbarqawi.000webhostapp.com/craftman/edituserdata.php";

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    ActionBarDrawerToggle mToggle;
    SessionManager sessionManager;
    String getId, getEmail,getName,getType;
    EditText tv_name , phone_ed;
    TextView email, address;



    private Menu action;

    private static final Pattern USER_NAME = Pattern.compile("[a-zA-Z]{2,15}");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sessionManager =  new SessionManager(this);
        sessionManager.checkLogin();
        tv_name = findViewById(R.id.tv_name);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        phone_ed = findViewById(R.id.phone);
        tv_name.setFocusableInTouchMode(false);
        phone_ed.setFocusableInTouchMode(false);


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

        HashMap<String, String> user = sessionManager.getUserDetail();

        getId = user.get(SessionManager.ID);
        getEmail = user.get(SessionManager.EMAIL);
        getName = user.get(SessionManager.NAME);
        getType = user.get(SessionManager.TYPE);

        getUserDetail();
    }

    private void getUserDetail() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, CUSTOMER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("read");

                    if (success.equals("1"))
                    {
                        for(int i = 0;i <jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            String str_id = object.getString("id").trim();
                            String str_first_name = object.getString("name").trim();
                            String str_address = object.getString("address").trim();
                            String str_phone = object.getString("phone").trim();
                            String str_email = object.getString("email").trim();
                            tv_name.setText(str_first_name);
                            email.setText(str_email);
                            phone_ed.setText(str_phone);
                            address.setText(str_address);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
//                    Toast.makeText(Profile.this, "Error Reading Details in json"+e.toString(),Toast.LENGTH_SHORT).show();
                    Toasty.error(Profile.this,"Error Reading Details in json"+e.toString(), Toasty.LENGTH_LONG,true).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
//                Toast.makeText(Profile.this, "Error Reading Details"+error.toString(),Toast.LENGTH_SHORT).show();
                Toasty.error(Profile.this,"Error Reading Details in json"+error.toString(), Toasty.LENGTH_LONG,true).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params =  new HashMap<>();
                params.put("id",getId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void saveDetails() {

        final String name = this.tv_name.getText().toString().trim();
        final String phone = this.phone_ed.getText().toString().trim();
        final String id = getId;

        boolean checkfirstname = false;
        boolean checkphone = false;

        //check the name if it not empty
        if (name.isEmpty()) {
            this.tv_name.setError("Name can't be Empty");
        } else if (!USER_NAME.matcher(name).matches()) {
            this.tv_name.setError("Name is Wrong format it should be Characters without numbers ");
        } else {
            this.tv_name.setError(null);
            checkfirstname = true;
        }

        //check the phone if it not empty
        if (phone.isEmpty()) {
            this.phone_ed.setError("Phone can't be Empty");
        } else if (phone.length() != 10) {
            this.phone_ed.setError("Phone too short");
        } else {
            this.phone_ed.setError(null);
            checkphone = true;
        }
        if (checkfirstname  && checkphone ) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Saving...");
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");

                        if (success.equals("1")) {
//                            Toast.makeText(Profile.this, "Update Successful!", Toast.LENGTH_LONG).show();
                            Toasty.success(Profile.this,"Update Successful!", Toasty.LENGTH_LONG,true).show();
                            sessionManager.createSession(getName,getEmail,getId,getType);
                            Intent i = new Intent(Profile.this, Profile.class);
                            startActivity(i);
                        }
                        else if (success.equals("0")) {
//                            Toast.makeText(Profile.this, "Update Failed!", Toast.LENGTH_LONG).show();
                            Toasty.error(Profile.this,"Update Failed!", Toasty.LENGTH_LONG,true).show();

                        }
                        else if (success.equals("2")) {
//                            Toast.makeText(Profile.this, "Change your Phone Number", Toast.LENGTH_LONG).show();
                            Toasty.error(Profile.this,"Change your Phone Number", Toasty.LENGTH_LONG,true).show();
                        }
                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
//                        Toast.makeText(Profile.this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
                        Toasty.error(Profile.this,"Error" + e.toString(), Toasty.LENGTH_LONG,true).show();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
//                    Toast.makeText(Profile.this, "Error" + error.toString(), Toast.LENGTH_LONG).show();
                    Toasty.error(Profile.this,"Error" + error.toString(), Toasty.LENGTH_LONG,true).show();
                }
            }
            ) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name);
                    params.put("phone", phone);
                    params.put("id", id);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        action = menu;
        action.findItem(R.id.menu_save).setVisible(false);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_edit:
                tv_name.setFocusableInTouchMode(true);
                phone_ed.setFocusableInTouchMode(true);
                InputMethodManager im =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.showSoftInput(tv_name, InputMethodManager.SHOW_IMPLICIT);
                action.findItem(R.id.menu_edit).setVisible(false);
                action.findItem(R.id.menu_save).setVisible(true);
                return true;
            case R.id.menu_save:
                saveDetails();
                action.findItem(R.id.menu_edit).setVisible(true);
                action.findItem(R.id.menu_save).setVisible(false);
                tv_name.setFocusableInTouchMode(false);
                phone_ed.setFocusableInTouchMode(false);
                tv_name.setFocusable(false);
                phone_ed.setFocusable(false);
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
                intent =  new Intent(Profile.this,Categories.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.list:
//                intent =  new Intent(Categories.this,CustomerList.class);
//                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.my_profile:
                intent =  new Intent(Profile.this,Profile.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.logout:
                sessionManager.logout();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.add_post:
                intent =  new Intent(Profile.this,AddPost.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
        }
        return true;
    }
}