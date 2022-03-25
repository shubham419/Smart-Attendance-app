package com.example.smartattendance.MainFragment;


import android.os.Build;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.smartattendance.MainActivity;
import com.example.smartattendance.R;
import com.example.smartattendance.databinding.FragmentProfileBinding;

import java.util.Objects;


public class Profile extends Fragment  {


    public Profile() {
        // Required empty public constructor
    }


    FragmentProfileBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       binding = FragmentProfileBinding.inflate(inflater, container, false);


       binding.backBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               goBack();
           }
       });




        return binding.getRoot();
    }

    private void goBack() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = requireActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primaryColor));
        }
        requireActivity().getActionBar().show();
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout,new TimeTable());
        transaction.commit();
    }

}