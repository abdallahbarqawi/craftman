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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    CardView report, users;

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    ActionBarDrawerToggle mToggle;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        report = findViewById(R.id.report);
        users = findViewById(R.id.users);

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(MainActivity.this,Reports.class);
                startActivity(intent);
            }
        });
        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(MainActivity.this,Users.class);
                startActivity(intent);
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
            case R.id.my_home:
                intent =  new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.users:
                intent =  new Intent(MainActivity.this,Users.class);
                startActivity(intent);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.report:
                intent =  new Intent(MainActivity.this,Reports.class);
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