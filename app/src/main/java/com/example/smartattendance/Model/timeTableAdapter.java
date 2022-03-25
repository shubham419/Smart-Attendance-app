package com.example.smartattendance.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartattendance.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class timeTableAdapter extends FirebaseRecyclerAdapter<timeTable, timeTableAdapter.holder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public timeTableAdapter(@NonNull @NotNull FirebaseRecyclerOptions<timeTable> options ) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull timeTableAdapter.holder holder, int position, @NonNull @NotNull timeTable model) {



            holder.subName.setText(model.getSubjectName());
            holder.subTime.setText(model.getTime());
            Picasso.get().load(model.getImageUrl()).into(holder.subImage);

    }

    @NonNull
    @NotNull
    @Override
    public holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sample_timetable, parent, false);
        return new holder(view);
    }

    class holder extends RecyclerView.ViewHolder{

        ImageView subImage;
        TextView subName,subTime;

        public holder(@NonNull @NotNull View itemView) {
            super(itemView);
            subImage = (ImageView) itemView.findViewById(R.id.subImage);
            subName = (TextView) itemView.findViewById(R.id.subName);
            subTime = (TextView) itemView.findViewById(R.id.subTime);
        }
    }

}
