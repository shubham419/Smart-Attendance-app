package com.example.smartattendance.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.smartattendance.MainActivity;
import com.example.smartattendance.MainFragment.Attendance;
import com.example.smartattendance.Model.attendanceReportAdapter;
import com.example.smartattendance.Model.attendanceSessionData;
import com.example.smartattendance.Model.classInfo;
import com.example.smartattendance.Model.student;
import com.example.smartattendance.R;
import com.example.smartattendance.databinding.ActivityAttendanceSessionBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class AttendanceSession extends AppCompatActivity {

    ActivityAttendanceSessionBinding binding;
    private char post;
    DatabaseReference db;
    private attendanceSessionData sessionData;
    boolean flage = true;
    attendanceReportAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAttendanceSessionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.attendanceSessionActivity));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
      //      getWindow().setStatusBarColor(ContextCompat.getColor(AttendanceSession.this,R.color.white));// set status background white

        }
        Objects.requireNonNull(getSupportActionBar()).hide();

        db = FirebaseDatabase.getInstance().getReference();
        post = Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()).charAt(0);
        ImageView imageView = (ImageView) findViewById(R.id.gifImageView);
        Glide.with(AttendanceSession.this).asGif().load(R.raw.student_gif).into(imageView);

        db.child("attendanceSession").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                sessionData = snapshot.getValue(attendanceSessionData.class);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(AttendanceSession.this, "problem in attendance session data ", Toast.LENGTH_SHORT).show();
            }
        });


        if (post == 'z') {
            binding.textView.setText("Correct Value");
        }

        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                String time = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(l));
                binding.textView6.setText(String.valueOf(time));
                if (flage == true) {
                    binding.timerCardView.setCardElevation((float) 5);
                    flage = false;
                } else {
                    binding.timerCardView.setCardElevation((float) 0);
                    flage = true;
                }
            }

            @Override
            public void onFinish() {
                //   binding.gif.setVisibility(View.GONE);

                if (post == 'z') {
                    updateSessionStatus();
                    binding.text.setText(String.valueOf(sessionData.getA()));
                    binding.text.setTextSize((float) 35);
                    binding.text.setAllCaps(true);
                } else {
                    binding.text.setVisibility(View.GONE);
                    changeStudentLayout();
                }
                vibrate();
                stageTwoTimer();

            }
        }.start();

        binding.presentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPresentStudent();
            }
        });

        binding.absentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAbsentStudent();
            }
        });

    }


    private void stageTwoTimer() {

        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                String time = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(l));
                binding.textView6.setText(time);
                if (flage == true) {
                    binding.timerCardView.setCardElevation((float) 5);
                    flage = false;
                } else {
                    binding.timerCardView.setCardElevation((float) 0);
                    flage = true;
                }

            }

            @Override
            public void onFinish() {
                if (post == 'z') {
                    binding.text.setText(sessionData.getW());
                } else {
                    checkStudentResponse(1);
                    changeStudentLayout1();
                    binding.radioGroup.clearCheck();
                }
                vibrate();
                stageThreeTimer();
         //       Toast.makeText(AttendanceSession.this, "timer1", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(300);
        }

    }

    private void checkStudentResponse(int a) {
        int rbId = binding.radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(rbId);
        String studentAnswer = radioButton.getText().toString();
        String correctAnswer;
        if (a == 1) {
            correctAnswer = String.valueOf(sessionData.getA());
        } else {
            correctAnswer = sessionData.getW();
        }

        // Toast.makeText(this, studentAnswer, Toast.LENGTH_SHORT).show();
        if (!studentAnswer.equals(correctAnswer)) {
       //     Toast.makeText(this, "your response is right", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "your response is wrong \n please contact respective teacher", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(AttendanceSession.this, MainActivity.class);
            AttendanceSession.this.startActivity(intent);
            AttendanceSession.this.finish();
        }
    }

    private void stageThreeTimer() {
        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                String time = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(l));
                binding.textView6.setText(time);
                if (flage == true) {
                    binding.timerCardView.setCardElevation((float) 5);
                    flage = false;
                } else {
                    binding.timerCardView.setCardElevation((float) 0);
                    flage = true;
                }

            }

            @Override
            public void onFinish() {
                if(post != 'z'){
                    checkStudentResponse(0);
                    markStudentPresent();
                    rateLecture();
                }

              //  Toast.makeText(AttendanceSession.this, "timer1", Toast.LENGTH_SHORT).show();

                binding.timerCardView.setVisibility(View.GONE);
                binding.radioButtonCardView.setVisibility(View.GONE);
                binding.gifImageView.setVisibility(View.GONE);

                binding.reportLayout.setVisibility(View.VISIBLE);
                showAbsentStudent();
                setClassInfo();
                setRealTimeRating();
                setRealTimePresentStudentNumber();
            }
        }.start();
    }

    private void setRealTimePresentStudentNumber() {
    db.child("attendanceReport").child(sessionData.getDate()).child(sessionData.getSubject()+" "+sessionData.getA()+sessionData.getW()).child("absent").addValueEventListener(new ValueEventListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
            int absentNo = (int) snapshot.getChildrenCount();
            int presentNo = 80 -absentNo;
            binding.presentNo.setText(Integer.toString(presentNo));
            binding.absentNo.setText(Integer.toString(absentNo));
        }

        @Override
        public void onCancelled(@NonNull @NotNull DatabaseError error) {

        }
    });
    }

    private void setRealTimeRating() {
        db.child("attendanceReport").child(sessionData.getDate()).child(sessionData.getSubject()+" "+sessionData.getA()+sessionData.getW()).child("rating").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                int rating = 0 ;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    int  i = dataSnapshot.getValue(Integer.class);
                    rating += i;
                }
             //   Toast.makeText(AttendanceSession.this, "" +rating, Toast.LENGTH_SHORT).show();
                if(rating!=0) {
                    float average = (float) rating / snapshot.getChildrenCount();
                   String avg = String.format("%.01f", average);
                    binding.ratingNo.setText(avg);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        }


    private void rateLecture() {

        AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceSession.this);
        Toast.makeText(AttendanceSession.this, "your attendance marked present successfully", Toast.LENGTH_SHORT).show();

        final View customLayout = getLayoutInflater().inflate(R.layout.rating_dialog,null);

        final int[] rating = {0};
        ImageView firstStar = customLayout.findViewById(R.id.firstStar), secondStar = customLayout.findViewById(R.id.secondStar), thirdStar = customLayout.findViewById(R.id.thirdStar)
                , fourthStar = customLayout.findViewById(R.id.fourthStar),fifthStar = customLayout.findViewById(R.id.fifthStar);

        firstStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating[0] = 1;
                firstStar.setImageResource(R.drawable.ic_star);
                secondStar.setImageResource(R.drawable.ic_blank_star);
                thirdStar.setImageResource(R.drawable.ic_blank_star);
                fourthStar.setImageResource(R.drawable.ic_blank_star);
                fifthStar.setImageResource(R.drawable.ic_blank_star);

            }
        });

        secondStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating[0] = 2;
                firstStar.setImageResource(R.drawable.ic_star);
                secondStar.setImageResource(R.drawable.ic_star);
                thirdStar.setImageResource(R.drawable.ic_blank_star);
                fourthStar.setImageResource(R.drawable.ic_blank_star);
                fifthStar.setImageResource(R.drawable.ic_blank_star);

            }
        });

        thirdStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating[0] = 3;
                firstStar.setImageResource(R.drawable.ic_star);
                secondStar.setImageResource(R.drawable.ic_star);
                thirdStar.setImageResource(R.drawable.ic_star);
                fourthStar.setImageResource(R.drawable.ic_blank_star);
                fifthStar.setImageResource(R.drawable.ic_blank_star);
            }
        });

        fourthStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating[0] = 4;
                firstStar.setImageResource(R.drawable.ic_star);
                secondStar.setImageResource(R.drawable.ic_star);
                thirdStar.setImageResource(R.drawable.ic_star);
                fourthStar.setImageResource(R.drawable.ic_star);
                fifthStar.setImageResource(R.drawable.ic_blank_star);
            }
        });

        fifthStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating[0] = 5;
                firstStar.setImageResource(R.drawable.ic_star);
                secondStar.setImageResource(R.drawable.ic_star);
                thirdStar.setImageResource(R.drawable.ic_star);
                fourthStar.setImageResource(R.drawable.ic_star);
                fifthStar.setImageResource(R.drawable.ic_star);
            }
        });

        builder.setView(customLayout);
        builder.setMessage("Rate Lecture experience");

        builder.setCancelable(false);
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                assert email != null;
                db.child("attendanceReport").child(sessionData.getDate()).child(sessionData.getSubject()+" "+sessionData.getA()+sessionData.getW()).child("rating")
                        .child(email).setValue(rating[0]);


            }
        });


        builder.setNegativeButton("skip", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();



    }

    private void setClassInfo() {
        binding.teacherNameTv.setText(sessionData.getTeacherName());
        binding.subjectNameTv.setText(sessionData.getSubject());
        binding.timeTv.setText(sessionData.getTime());
    }

    private void showPresentStudent() {

        binding.presentTv.setTextSize((float) 20);
        binding.presentTv.setTextColor(this.getResources().getColor(R.color.green_absent_color));
        binding.absentTv.setTextColor(this.getResources().getColor(R.color.black));
        binding.absentTv.setTextSize((float) 17);

        String subjectName = sessionData.getSubject()+" "+sessionData.getA()+sessionData.getW();

        Query path = db.child("attendanceReport").child(sessionData.getDate()).child(subjectName).child("present").orderByChild("seatNumber");
        FirebaseRecyclerOptions<student> options =
                new FirebaseRecyclerOptions.Builder<student>()
                        .setQuery(path, student.class)
                        .build();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(AttendanceSession.this));
        adapter = new attendanceReportAdapter(options,1,post,AttendanceSession.this,subjectName,sessionData.getDate());
        binding.recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void showAbsentStudent() {

        binding.presentTv.setTextSize((float) 17);
        binding.presentTv.setTextColor(this.getResources().getColor(R.color.black));
        binding.absentTv.setTextColor(this.getResources().getColor(R.color.orange_absent_color));
        binding.absentTv.setTextSize((float) 20);

        String subjectName = sessionData.getSubject()+" "+sessionData.getA()+sessionData.getW();

        Query path = db.child("attendanceReport").child(sessionData.getDate()).child(subjectName).child("absent").orderByChild("seatNumber");

        FirebaseRecyclerOptions<student> options =
                new FirebaseRecyclerOptions.Builder<student>()
                        .setQuery(path, student.class)
                        .build();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(AttendanceSession.this));
        adapter = new attendanceReportAdapter(options,0,post,AttendanceSession.this,subjectName,sessionData.getDate());
        binding.recyclerView.setAdapter(adapter);
        adapter.startListening();

    }


    private void changeStudentLayout1() {

        Random random = new Random();
        //int r = random.nextInt(4);
        //0=a, 1=b, 2=c,3=d
        String[] arr = {sessionData.getW(), sessionData.getX(), sessionData.getY(), sessionData.getZ()};
        int a = 10, b = 10, c = 10, d = 10;

        boolean flag = false;
        while (!flag) {
            int r = random.nextInt(4);
            if (a == 10) {
                a = r;

            }
            if (r != a && b == 10) {
                b = r;
                continue;
            }
            if (r != a && r != b && c == 10) {
                c = r;
                continue;
            }
            if (r != a && r != b && r != c && d == 10) {
                d = r;
                flag = true;
            }

        }

        binding.radioButton.setText(arr[a]);
        binding.radioButton2.setText(arr[b]);
        binding.radioButton3.setText(arr[c]);
        binding.radioButton4.setText(arr[d]);


    }


    private void changeStudentLayout() {


        binding.radioGroup.setVisibility(View.VISIBLE);
        Random random = new Random();
        //int r = random.nextInt(4);
        //0=a, 1=b, 2=c,3=d


        int[] arr = {sessionData.getA(), sessionData.getB(), sessionData.getC(), sessionData.getD()};
        int a = 10, b = 10, c = 10, d = 10;

        boolean flag = false;
        while (!flag) {
            int r = random.nextInt(4);
            if (a == 10) {
                a = r;

            }
            if (r != a && b == 10) {
                b = r;
                continue;
            }
            if (r != a && r != b && c == 10) {
                c = r;
                continue;
            }
            if (r != a && r != b && r != c && d == 10) {
                d = r;
                flag = true;
            }

        }

        binding.radioButton.setText(String.valueOf(arr[a]));
        binding.radioButton2.setText(String.valueOf(arr[b]));
        binding.radioButton3.setText(String.valueOf(arr[c]));
        binding.radioButton4.setText(String.valueOf(arr[d]));

    }


    private void markStudentPresent() {

        String id = FirebaseAuth.getInstance().getUid();

        DatabaseReference path =   db.child("attendanceReport").child(sessionData.getDate()).child(sessionData.getSubject()+" "+sessionData.getA()+sessionData.getW());
                  //  .child("absent");//.child(FirebaseAuth.getInstance().getUid());

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


    private void updateSessionStatus() {
        db.child("attendanceSessionStatus").setValue("inactive");
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(AttendanceSession.this, LoginActivity.class);
            AttendanceSession.this.startActivity(intent);
            AttendanceSession.this.finish();
        }

        return true;
    }

}