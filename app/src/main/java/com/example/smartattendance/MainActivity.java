package com.example.smartattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.smartattendance.Activity.LoginActivity;
import com.example.smartattendance.Activity.profileActivity;
import com.example.smartattendance.MainFragment.Attendance;
import com.example.smartattendance.MainFragment.ProblemRaised;
import com.example.smartattendance.MainFragment.Profile;
import com.example.smartattendance.MainFragment.Report;
import com.example.smartattendance.MainFragment.TimeTable;
import com.example.smartattendance.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(intent);
            MainActivity.this.finish();
        }


        if (FirebaseAuth.getInstance().getCurrentUser().getEmail().charAt(0) == 'z') {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.framelayout, new Attendance());
            transaction.commit();
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.framelayout, new TimeTable());
            transaction.commit();
        }

        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                switch (item.getItemId()) {
                    case R.id.schedule:
                        transaction.replace(R.id.framelayout, new TimeTable());
                        break;
                    case R.id.attendance:
                        transaction.replace(R.id.framelayout, new Attendance());
                        break;
                    case R.id.report:
                        transaction.replace(R.id.framelayout, new Report());
                        break;
                    case R.id.profile:
                        transaction.replace(R.id.framelayout, new ProblemRaised());
                        break;
                }
                transaction.commit();

                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.upper_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(intent);
            MainActivity.this.finish();
        }

        if (item.getItemId() == R.id.profile) {
            char post = Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()).charAt(0);
            if (post != 'z') {
                Intent intent = new Intent(MainActivity.this, profileActivity.class);
                MainActivity.this.startActivity(intent);
            }
        }

        return true;
    }


}