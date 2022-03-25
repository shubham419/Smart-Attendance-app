package com.example.smartattendance.MainFragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.smartattendance.MainActivity;
import com.example.smartattendance.Model.attendanceReportAdapter;
import com.example.smartattendance.Model.student;
import com.example.smartattendance.R;
import com.example.smartattendance.databinding.FragmentReportBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class Report extends Fragment {



    public Report() {
        // Required empty public constructor
    }

    FragmentReportBinding binding;
    attendanceReportAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =  FragmentReportBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}