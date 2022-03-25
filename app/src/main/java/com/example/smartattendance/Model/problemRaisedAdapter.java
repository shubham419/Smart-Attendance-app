package com.example.smartattendance.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartattendance.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class problemRaisedAdapter extends RecyclerView.Adapter<problemRaisedAdapter.viewholder>{

    ArrayList<problem> list;
    Context context;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("problemMessage");
    problem problem;
    String seatNumber;

    public problemRaisedAdapter(ArrayList<problem> list, Context context, String seatNumber) {
        this.list = list;
        this.context = context;
        this.seatNumber =seatNumber;
    }




    @NonNull
    @NotNull
    @Override
    public viewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_problem_raised ,parent ,false);
        return new viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull problemRaisedAdapter.viewholder holder, int position) {

        problem = list.get(position);

        db.child(problem.getProblemTitle()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                problem realProblem = snapshot.getValue(problem.class);

                assert realProblem != null;
                holder.problemTv.setText(realProblem.getProblemTitle());
                holder.voteTv.setText(""+realProblem.getVote());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



            holder.upVoteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (problem.getVote() != 0) {
                        Toast.makeText(context, "Your vote is already taken", Toast.LENGTH_SHORT).show();
                    } else {

                        db.child(problem.getProblemTitle()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                problem realProblem = snapshot.getValue(problem.class);
                                assert realProblem != null;
                                int vote = realProblem.getVote() + 1;
                                db.child(problem.getProblemTitle()).child("vote").setValue(vote);
                                holder.voteTv.setText("" + vote);
                                pushAlgorithm();
                                pushAlgorithm();
                            }

                            private void pushAlgorithm() {
                                Random random = new Random();
                                int i =random.nextInt(80);
                                FirebaseDatabase.getInstance().getReference().child("problemReceivedToStudent").child(Integer.toString(i)).child(problem.getProblemTitle()).setValue(problem);

                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });

                        holder.upVoteIv.setVisibility(View.GONE);

                        FirebaseDatabase.getInstance().getReference().child("problemReceivedToStudent").child(seatNumber).child(problem.getProblemTitle()).child("vote").setValue(1);

                        Toast.makeText(context, "Your vote is taken", Toast.LENGTH_SHORT).show();

                    }
                }
            });


   //     holder.problemTv.setText(problemTitle);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        TextView problemTv, voteTv;

        ImageView upVoteIv;

        public viewholder(@NonNull @NotNull View itemView) {
            super(itemView);
            problemTv = itemView.findViewById(R.id.problemTv);
            voteTv = itemView.findViewById(R.id.voteTv);
            upVoteIv = (ImageView) itemView.findViewById(R.id.upVoteIv);
        }

    }

}
