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
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class Categories extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    ActionBarDrawerToggle mToggle;
    SessionManager sessionManager;
    CardView painting_services, construction, electrician_services, agricultural_services, plumbing_services, carpentry_services,gardening_services,car_maintenance,cleaning_services,other_services;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

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

        painting_services = findViewById(R.id.paintingservices);
        construction = findViewById(R.id.construction);
        electrician_services = findViewById(R.id.electricianservices);
        agricultural_services = findViewById(R.id.agriculturalservices);
        plumbing_services = findViewById(R.id.plumbingservices);
        carpentry_services = findViewById(R.id.carpentryservices);
        gardening_services = findViewById(R.id.gardeningservices);
        car_maintenance = findViewById(R.id.carmaintenance);
        cleaning_services = findViewById(R.id.cleaningservices);
        other_services = findViewById(R.id.otherservices);

        painting_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Categories.this, ChooseActivity.class);
                i.putExtra("category","1");
                startActivity(i);
            }
        });
        electrician_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Categories.this, ChooseActivity.class);
                i.putExtra("category","2");
                startActivity(i);
            }
        });
        construction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Categories.this, ChooseActivity.class);
                i.putExtra("category","3");
                startActivity(i);
            }
        });
        agricultural_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Categories.this, ChooseActivity.class);
                i.putExtra("category","4");
                startActivity(i);
            }
        });
        plumbing_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Categories.this, ChooseActivity.class);
                i.putExtra("category","5");
                startActivity(i);
            }
        });
        carpentry_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Categories.this, ChooseActivity.class);
                i.putExtra("category","6");
                startActivity(i);
            }
        });
        gardening_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Categories.this, ChooseActivity.class);
                i.putExtra("category","7");
                startActivity(i);
            }
        });
        car_maintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Categories.this, ChooseActivity.class);
                i.putExtra("category","8");
                startActivity(i);
            }
        });
        cleaning_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Categories.this, ChooseActivity.class);
                i.putExtra("category","9");
                startActivity(i);
            }
        });
        other_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Categories.this, ChooseActivity.class);
                i.putExtra("category","10");
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
                intent =  new Intent(Categories.this,Categories.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.list:
//                intent =  new Intent(Categories.this,CustomerList.class);
//                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.my_profile:
                intent =  new Intent(Categories.this,Profile.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.logout:
                sessionManager.logout();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.add_post:
                intent =  new Intent(Categories.this,AddPost.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
        }
        return true;
    }
}