package com.example.smartattendance.MainFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.smartattendance.Model.problem;
import com.example.smartattendance.Model.problemRaisedAdapter;


import com.example.smartattendance.databinding.FragmentProblemRaisedBinding;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;


public class ProblemRaised extends Fragment {


    public ProblemRaised() {
        // Required empty public constructor
    }

    FragmentProblemRaisedBinding binding;
    String problem;
    String seatNumber;

    ArrayList<com.example.smartattendance.Model.problem> list;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProblemRaisedBinding.inflate(inflater, container, false);

        char post = Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()).charAt(0);

        if(post!='z') {

            list = new ArrayList<>();
            String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();


            FirebaseDatabase.getInstance().getReference().child("seCompUser").child("student").child(uid).child("seatNumber").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    seatNumber = snapshot.getValue(String.class);

                    checkProblem(seatNumber);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

            binding.nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    problem = binding.problemEt.getText().toString();
                    if (!problem.equals("")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setMessage("Do you want to raise issue");

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(getActivity(), "Your problem is noted successfully \n please refresh page", Toast.LENGTH_SHORT).show();
                                binding.problemEt.setText("");
                                Random random = new Random();
                                int i = random.nextInt(99);
                                int j = random.nextInt(99);
                                String problemTitle = j + seatNumber + i;

                                FirebaseDatabase.getInstance().getReference().child("problemMessage").child(problemTitle).child("problemTitle").setValue(problem);
                                FirebaseDatabase.getInstance().getReference().child("problemMessage").child(problemTitle).child("vote").setValue(0);
                                com.example.smartattendance.Model.problem problemModel = new problem(problemTitle , 0);
                                FirebaseDatabase.getInstance().getReference().child("problemReceivedToStudent").child(seatNumber).child(problemTitle).setValue(problemModel);



                                for (int k = 0; k < 10; k++) {
                                    sendMessage(problemModel);
                                }
                            }
                        });

                        builder.show();

                    } else {
                        binding.problemEt.setError("Empty");
                    }
                }
            });
        }
        else{
            binding.demoTv.setText("This feature is not available for teachers yet.");
            binding.problemRv.setVisibility(View.GONE);
            binding.linearLayout.setVisibility(View.GONE);
            binding.messageTv.setVisibility(View.GONE);
            binding.tImage.setVisibility(View.VISIBLE);
        }

        return binding.getRoot();
    }

    private void checkProblem(String seatNumber) {

        FirebaseDatabase.getInstance().getReference().child("problemReceivedToStudent").child(seatNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    problem problemModel = snapshot1.getValue(problem.class);
                    list.add(problemModel);
                }


                if (!(list.size() == 1)) {
                    binding.demoTv.setVisibility(View.GONE);
                    list.remove(0);

                    showRecyclerView();
                }
            }


            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    private void showRecyclerView() {

        problemRaisedAdapter adapter = new problemRaisedAdapter(list, requireContext(),seatNumber);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.problemRv.setLayoutManager(layoutManager);

        binding.problemRv.setAdapter(adapter);


    }

    private void sendMessage(problem problemModel){

        Random random = new Random();
        int i =random.nextInt(80);
        FirebaseDatabase.getInstance().getReference().child("problemReceivedToStudent").child(Integer.toString(i)).child(problemModel.getProblemTitle()).setValue(problemModel);

    }


}