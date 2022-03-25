package com.example.smartattendance.MainFragment;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartattendance.Activity.AttendanceSession;
import com.example.smartattendance.Model.student;
import com.example.smartattendance.Model.timeTable;
import com.example.smartattendance.Model.timeTableAdapter;
import com.example.smartattendance.R;
import com.example.smartattendance.databinding.FragmentTimeTableBinding;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Objects;


public class TimeTable extends Fragment {


    public TimeTable() {
        // Required empty public constructor
    }


    FragmentTimeTableBinding binding;
    String getTimeTable;
    timeTableAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =FragmentTimeTableBinding.inflate(inflater, container, false);

       int fDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
       String today;
       // Calender.SUNDAY that is 1
       switch (fDay) {
            case Calendar.MONDAY:
                today = "monday";
                break;
           case Calendar.TUESDAY:
                 today = "tuesday";
                break;
            case Calendar.WEDNESDAY:
                today = "wednesday";
                break;
            case Calendar.THURSDAY:
                today = "thursday";
                break;
            case Calendar.FRIDAY:
                today = "friday";
                break;
            default:
                today = "monday";
                break;
        }

        binding.todayTv.setText(today);
        getTimeTable = today;

        binding.pickerArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(binding.dayView.getVisibility() == View.GONE) {
                   binding.dayView.setVisibility(View.VISIBLE);
                   binding.pickerArrow.setImageResource(R.drawable.ic_up_arrow);
               }
               else {
                   binding.dayView.setVisibility(View.VISIBLE);
                   binding.pickerArrow.setImageResource(R.drawable.ic_down_arrow);
               }

            }
        });

       binding.monday.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               binding.todayTv.setText("Monday");
               changeLayout("monday");
           }
       });

        binding.tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.todayTv.setText("Tuesday");
                changeLayout("tuesday");
            }
        });

        binding.wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.todayTv.setText("Wednesday");
                changeLayout("wednesday");
            }
        });

        binding.thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.todayTv.setText("Thursday");
                changeLayout("thursday");
            }
        });

        binding.friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.todayTv.setText("Friday");
                changeLayout("friday");
            }
        });



        showInRecyclerView();





        //   binding.mainL.setBackground(paint);


        return binding.getRoot();
    }

    private void showInRecyclerView() {

        Query path = FirebaseDatabase.getInstance().getReference().child("timeTable").child(getTimeTable).orderByKey();
        FirebaseRecyclerOptions<timeTable> options =
                new FirebaseRecyclerOptions.Builder<timeTable>()
                        .setQuery(path, timeTable.class)
                        .build();

        binding.recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
       // adapter = new timeTableAdapter(options, requireContext());
        timeTableAdapter adapter = new timeTableAdapter(options );
        binding.recycler.setAdapter(adapter);
        adapter.startListening();

    }

    private void changeLayout(String day) {
        getTimeTable = day;
        showInRecyclerView();
   //     TransitionManager.beginDelayedTransition(binding.expandView, new AutoTransition());
        binding.pickerArrow.setImageResource(R.drawable.ic_down_arrow);
        binding.dayView.setVisibility(View.GONE);
    }
}








//---------------------------------------------------------------------------------------------

//    ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
//        @Override
//        public Shader resize(int width, int height) {
//            LinearGradient linearGradient = new LinearGradient(0, 0, width, height,
//                    new int[] {
//                            0xD5FFA000,
//
//                            0xFFFFFFFF,
//                    }, //substitute the correct colors for these
//                    new float[] {
//                            0.0f, 10.0f },
//                    Shader.TileMode.REPEAT);
//            return linearGradient;
//        }
//    };
//    PaintDrawable paint = new PaintDrawable();
//        paint.setShape(new RectShape());
//                paint.setShaderFactory(shaderFactory);
