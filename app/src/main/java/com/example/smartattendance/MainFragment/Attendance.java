package com.example.smartattendance.MainFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.smartattendance.Activity.AttendanceSession;
import com.example.smartattendance.Model.attendanceSessionData;
import com.example.smartattendance.Model.classInfo;
import com.example.smartattendance.R;
import com.example.smartattendance.databinding.FragmentAttendanceBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class Attendance extends Fragment {


    public Attendance() {
        // Required empty public constructor
    }

    FragmentAttendanceBinding binding;
    LocationManager locationManager;
    private char post;
    private String mLatitude, mLongitude;
    private FusedLocationProviderClient fusedLocationClient;
    FirebaseAuth auth;
    classInfo classInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAttendanceBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        String email = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        assert email != null;
        post = email.charAt(0) ;




        if (post == 'z') {
            binding.attendancebtn.setVisibility(View.VISIBLE);
            FirebaseDatabase.getInstance().getReference().child("user").child("teacher").child(auth.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                     classInfo = snapshot.getValue(classInfo.class);
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    Toast.makeText(getActivity(), "problem in getting subject name", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (post != 'z') {
            FirebaseDatabase.getInstance().getReference()
                    .child("attendanceSessionStatus")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            String status = snapshot.getValue(String.class);
                            assert status != null;
                            if (status.equals("active")) {
                                binding.attendancebtnText.setText("Give Attendance");
                                binding.attendancebtn.setVisibility(View.VISIBLE);
                                binding.studentIv.setVisibility(View.GONE);
                                binding.studentTv.setVisibility(View.GONE);
                            } else {
                                binding.attendancebtn.setVisibility(View.GONE);
                                binding.studentIv.setVisibility(View.VISIBLE);
                                binding.studentTv.setVisibility(View.VISIBLE);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            Toast.makeText(getActivity(), "problem in attendance", Toast.LENGTH_SHORT).show();
                        }
                    });
        }








        binding.attendancebtnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
                if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    getLocation();

                } else {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                                    , Manifest.permission.ACCESS_COARSE_LOCATION}, 19);
                }
            }
        });


        return binding.getRoot();
    }


    private void writeInDatabaseTeacherOnly() {


        Random random = new Random();

        int a = random.nextInt(99);
        int b = random.nextInt(99);
        int c = random.nextInt(99);
        int d = random.nextInt(99);
        char w = (char) (random.nextInt(26) + 'a');
        char x = (char) (random.nextInt(26) + 'a');
        char y = (char) (random.nextInt(26) + 'a');
        char z = (char) (random.nextInt(26) + 'a');


        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        classInfo.setTime(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));

        DatabaseReference path = FirebaseDatabase.getInstance().getReference().child("attendanceReport").child(date).child(classInfo.getSubject()+" "+ a + w);

        attendanceSessionData data = new attendanceSessionData(mLatitude, mLongitude, a, b, c, d, "" + w, "" + x, "" + y, "" + z, classInfo.getName(), classInfo.getSubject(),classInfo.getTime(),date);

        FirebaseDatabase.getInstance().getReference().child("seCompUser").child("student").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                path.child("absent").setValue(snapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        path.child("classInfo").setValue(classInfo);

        FirebaseDatabase.getInstance().getReference().child("attendanceSessionStatus").setValue("active");

        FirebaseDatabase.getInstance().getReference().child("attendanceSession")
                .setValue(data).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

//    private String getSubjectName() {
//        String id = auth.getUid();
//
//        FirebaseDatabase.getInstance().getReference().child("user").child("teacher").child(id)
//                .child("subject").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                    String sub = snapshot.getValue(String.class);
//                    setSubjectValue(sub);
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//                Toast.makeText(getActivity(), "problem in getting subject name", Toast.LENGTH_SHORT).show();
//            }
//        });
//        return subject;
//    }


    @SuppressLint("MissingPermission")
    private void getLocation() {
        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            LocationRequest locationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                    .setInterval(1000)
                    .setFastestInterval(100)
                    .setNumUpdates(1)
                    //      .setMaxWaitTime(TimeUnit.MINUTES.toMillis(2))
                    ;

            LocationCallback locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull @NotNull LocationResult locationResult) {

                    Location location = locationResult.getLastLocation();

                    if (location != null) {
                        mLatitude = String.valueOf(location.getLatitude());
                        mLongitude = String.valueOf(location.getLongitude());

                        if (post == 'z') {
                            writeInDatabaseTeacherOnly();
                        }
                        Intent intent = new Intent(getActivity(), AttendanceSession.class);
                        requireActivity().startActivity(intent);
                    }

                }
            };
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {

        if (requestCode == 19 && grantResults.length > 0 && (grantResults[0] + grantResults[1]
                == PackageManager.PERMISSION_GRANTED)) {
            getLocation();
        } else {
            Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}

