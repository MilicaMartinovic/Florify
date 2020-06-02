package com.example.florify.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.florify.R;
import com.example.florify.Session;
import com.example.florify.UserProfileActivity;
import com.example.florify.db.services.ApproveConnectionRequestService;
import com.example.florify.db.services.DenyConnectionRequestService;
import com.example.florify.listeners.OnApproveConnectionRequest;
import com.example.florify.listeners.OnDenyConnectionRequest;
import com.example.florify.models.User;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class ReyclerViewConnectionRequestsAdapter
        extends RecyclerView.Adapter<ReyclerViewConnectionRequestsAdapter.ViewHolderConnectionRequests> {

    private ArrayList<User> users = new ArrayList<>();
    private Context context;

    public ReyclerViewConnectionRequestsAdapter(ArrayList<User> users, Context context)
    {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderConnectionRequests onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.connection_card_view, parent, false);
        ViewHolderConnectionRequests holder = new ViewHolderConnectionRequests(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderConnectionRequests holder, int position) {
        Glide.with(context)
                .asBitmap()
                .load(users.get(position).imageUrl)
                .into(holder.roundedImageView);

        holder.txtUsername.setText(users.get(position).username);
        holder.txtmotherland.setText(users.get(position).motherland);
        holder.txtBadge.setText(users.get(position).badge);
        holder.user = users.get(position);
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }


    public class ViewHolderConnectionRequests extends RecyclerView.ViewHolder
            implements OnApproveConnectionRequest, OnDenyConnectionRequest {

        public RoundedImageView roundedImageView;
        public TextView txtUsername;
        public TextView txtmotherland;
        public TextView txtBadge;
        public Button btnApprove, btnDelete;
        public LinearLayout parent;
        public TextView approved, denied;

        public User user;


        public ViewHolderConnectionRequests(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.connectionRequestsLayoutParent);
            roundedImageView = itemView.findViewById(R.id.imgProfileConnectionRequests);
            txtmotherland = itemView.findViewById(R.id.txtMotherlandConnectionRequests);
            txtUsername = itemView.findViewById(R.id.txtUsernameConnectionRequests);
            txtBadge = itemView.findViewById(R.id.txtBadgeConnectionRequests);
            btnApprove = itemView.findViewById(R.id.btnConnectionRequestsView);
            btnDelete = itemView.findViewById(R.id.btnConnectionRequestsDelete);
            approved = itemView.findViewById(R.id.txtConnectionCardApproved);
            denied = itemView.findViewById(R.id.txtConnectionCardRemovedRequest);

            btnApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ApproveConnectionRequestService(
                            ViewHolderConnectionRequests.this)
                            .execute(new Session(context).getUserId(), user.id
                            );
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DenyConnectionRequestService(
                            ViewHolderConnectionRequests.this)
                            .execute(new Session(context).getUserId(), user.id
                            );
                }
            });

            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.putExtra("userId", user.id);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onApprovedConnectionRequets(boolean success) {
            if(success) {
                btnApprove.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                approved.setVisibility(View.VISIBLE);
            }
            else {
                Toast.makeText(context,
                        "Could not approve request. Try again.",
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onDeniedConnectionRequest(Boolean success) {
            if(success) {
                btnApprove.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
                denied.setVisibility(View.VISIBLE);
            }
            else {
                Toast.makeText(context,
                        "Could not remove request. Try again.",
                        Toast.LENGTH_SHORT).show();

            }
        }
    }
}
