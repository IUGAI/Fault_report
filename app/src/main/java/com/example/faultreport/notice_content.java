package com.example.faultreport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.navigation.NavigationView;

public class notice_content extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Spinner spinner;

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){

            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)){
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_content);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.custom_imageview, null);
        getSupportActionBar().setCustomView(v);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigatorview);
        navigationView.bringToFront();
        drawerToggle  = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.report_contens: {
                                Intent intent = new Intent(notice_content.this, ReportContent.class);
                                startActivity(intent);
                                break;
                            }
                            case R.id.logout:{
                                Intent intent1 = new Intent(notice_content.this,login.class);
                                startActivity(intent1);
                                break;
                            }
                            case R.id.reportwrite:{
                                Intent intent2 = new Intent(notice_content.this,Main.class);
                                startActivity(intent2);
                                break;
                            }
                            case R.id.notice:{
                                Intent intent3 = new Intent(notice_content.this,notice_content.class);
                                startActivity(intent3);
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                        return false;
                    }
                });

        spinner = findViewById(R.id.belongsp);
        String belong[] = {"선택", "병원장실", "교수실"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spineritems, R.id.txtsp, belong);
        spinner.setAdapter(arrayAdapter);
    }
}