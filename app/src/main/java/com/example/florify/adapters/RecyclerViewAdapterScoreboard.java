package com.example.florify.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.florify.R;
import com.example.florify.Session;
import com.example.florify.models.ScoreboardUsersVM;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class RecyclerViewAdapterScoreboard extends RecyclerView.Adapter<RecyclerViewAdapterScoreboard.ViewHolder> {

    private ArrayList<ScoreboardUsersVM> usersVMS = new ArrayList<>();
    private Context context;
    private Session session;

    public RecyclerViewAdapterScoreboard(ArrayList<ScoreboardUsersVM> users, Context context) {
        this.usersVMS = users;
        this.context = context;
        session = new Session(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.scoreboard_card,
                parent,
                false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide
            .with(context)
            .load(usersVMS.get(position).imageUrl)
            .into(holder.imageView);

        holder.txtScore.setText(Integer.toString(usersVMS.get(position).contributionScore));
        holder.txtName.setText(usersVMS.get(position).username);
        if(position % 2 == 0)
            holder.parent
                    .setBackgroundColor(
                            context.getResources().getColor(R.color.ligntBlue, null));
    }


    @Override
    public int getItemCount() {
        return this.usersVMS.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView imageView;
        TextView txtName, txtScore;
        RelativeLayout parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgScoreBoardProfile);
            txtName = itemView.findViewById(R.id.txtScoreboardName);
            txtScore = itemView.findViewById(R.id.txtScoreboardScore);
            parent = itemView.findViewById(R.id.scoreboard_card_parent);
        }
    }
}
