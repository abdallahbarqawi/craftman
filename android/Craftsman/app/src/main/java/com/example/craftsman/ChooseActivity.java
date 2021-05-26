package com.example.craftsman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

public class ChooseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    CardView work, service;

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    ActionBarDrawerToggle mToggle;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

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

        work = findViewById(R.id.work);
        service = findViewById(R.id.service);

        Intent i = getIntent();
        String category = i.getStringExtra("category");

        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChooseActivity.this, Works.class);
                i.putExtra("category",category);
                startActivity(i);
            }
        });
        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ChooseActivity.this, Services.class);
                i.putExtra("category",category);
                startActivity(i);
            }
        });
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
            case R.id.home:
                intent =  new Intent(ChooseActivity.this,Categories.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.list:
//                intent =  new Intent(Categories.this,CustomerList.class);
//                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.my_profile:
                intent =  new Intent(ChooseActivity.this,Profile.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.logout:
                sessionManager.logout();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.add_post:
                intent =  new Intent(ChooseActivity.this,AddPost.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
        }
        return true;
    }
}