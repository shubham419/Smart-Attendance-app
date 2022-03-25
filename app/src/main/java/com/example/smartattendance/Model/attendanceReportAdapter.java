package com.example.smartattendance.Model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartattendance.MainActivity;
import com.example.smartattendance.MainFragment.Attendance;
import com.example.smartattendance.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class attendanceReportAdapter extends FirebaseRecyclerAdapter<student, attendanceReportAdapter.holder> {


    // 0 for absent student
    // 1 for present student
    int color;
    char post;
    Context context;
    String subjectName, date;

    public attendanceReportAdapter(@NonNull @NotNull FirebaseRecyclerOptions<student> options, int color, char post, Context context, String subjectName, String date) {
        super(options);
        this.color = color;
        this.post = post;
        this.context = context;
        this.subjectName = subjectName;
        this.date = date;
    }

    public attendanceReportAdapter(FirebaseRecyclerOptions<student> options, int i, char t, Context requireContext) {
        super(options);
        this.color = i;
        this.post = t;
        this.context = requireContext;

    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull attendanceReportAdapter.holder holder, int position, @NonNull @NotNull student model) {

        holder.studentName.setText(model.getName());
        holder.seatNo.setText(String.valueOf(model.getSeatNumber()));
        if (color == 0) {
            holder.cardView.setBackgroundColor(Color.parseColor("#fe5722"));
            holder.attendanceStatusTv.setText("Absent");
        } else {
            holder.cardView.setBackgroundColor(Color.parseColor("#00796a"));
            holder.attendanceStatusTv.setText("Present");
            holder.attendanceStatusIv.setImageResource(R.drawable.ic_check);
        }

        if (post == 'z') {
//            TypedValue outValue = new TypedValue();
//            context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
//            holder.itemView.setBackgroundResource(outValue.resourceId);

            holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if (color == 0) {
                        showDialogToMarkPresent(model);
                    } else {
                        showDialogToMarkAbsent(model);
                    }


                    return true;
                }
            });
        }


    }

    private void showDialogToMarkAbsent(student model) {
        String id = model.getUid();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(model.getName());
        builder.setMessage("Do you want to mark student Absent");


        builder.setPositiveButton("Processed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseReference path = FirebaseDatabase.getInstance().getReference().child("attendanceReport").child(date).child(subjectName);

                path.child("present").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        path.child("absent").child(id).setValue(snapshot.getValue());
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    }
                });

                path.child("present").child(id).removeValue();

            }
        });

        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

    }

    private void showDialogToMarkPresent(student model) {
        String id = model.getUid();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(model.getName());
        builder.setMessage("Do you want to mark student Present");


        builder.setPositiveButton("Processed", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DatabaseReference path = FirebaseDatabase.getInstance().getReference().child("attendanceReport").child(date).child(subjectName);

                path.child("absent").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        path.child("present").child(id).setValue(snapshot.getValue());
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    }
                });

                path.child("absent").child(id).removeValue();

            }
        });


        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


    @NonNull
    @NotNull
    @Override
    public holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sample_attendance_report, parent, false);
        return new holder(view);
    }

    class holder extends RecyclerView.ViewHolder {

        TextView studentName, seatNo, attendanceStatusTv;
        ImageView attendanceStatusIv;
        CardView cardView;

        public holder(@NonNull @NotNull View itemView) {
            super(itemView);
            studentName = (TextView) itemView.findViewById(R.id.student_name);
            seatNo = (TextView) itemView.findViewById(R.id.seat_no);
            cardView = (CardView) itemView.findViewById(R.id.sampleLayoutCv);
            attendanceStatusTv = (TextView) itemView.findViewById(R.id.attendanceStatusTv);
            attendanceStatusIv = (ImageView) itemView.findViewById(R.id.attendanceStatusIv);

        }
    }
}
