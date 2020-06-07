package com.example.florify;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.florify.db.services.FetchMyPostsViewsAndLIkes;
import com.example.florify.db.services.FetchPostsService;
import com.example.florify.db.services.FetchUserService;
import com.example.florify.db.services.RequestConnectionService;
import com.example.florify.dialogs.ContributionsPopup;
import com.example.florify.dialogs.OnContributionsClose;
import com.example.florify.helpers.ContributionScoreCalc;
import com.example.florify.listeners.OnFetchMyPostsViewsAndLikesComplted;
import com.example.florify.listeners.OnFetchPostsCompleted;
import com.example.florify.listeners.OnFetchUserCompleted;
import com.example.florify.listeners.OnRequestedConnectionService;
import com.example.florify.models.Post;
import com.example.florify.models.User;
import com.makeramen.roundedimageview.RoundedImageView;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity implements OnFetchUserCompleted,
        OnRequestedConnectionService, OnFetchPostsCompleted, OnContributionsClose, OnFetchMyPostsViewsAndLikesComplted {
    private TextView txtProfileName;
    private TextView txtProfileLocation;
    private TextView txtProfileLevel;
    private TextView txtProfileContributionScore;
    private TextView txtProfilePosts;
    private TextView txtProfileLikes;
    private TextView txtProfileViews;
    private TextView txtProfileConnections;
    private ImageButton imgAddConnection, imgConnectionAdded;
    private Button viewContributions;
    private Session session;
    private User user;
    private DialogInterface.OnClickListener dialogClickListener;
    private AlertDialog alertDialog;
    private RoundedImageView profileImage;
    private ContributionsPopup contributionsPopup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        session = new Session(this);

        txtProfileName = findViewById(R.id.txtProfileName);
        txtProfileLocation = findViewById(R.id.txtProfileLocation);
        txtProfileLevel = findViewById(R.id.txtProfileLevel);
        txtProfileContributionScore = findViewById(R.id.txtProfileContributionScore);
        txtProfilePosts = findViewById(R.id.txtProfilePosts);
        txtProfileLikes = findViewById(R.id.txtProfileLikes);
        txtProfileViews = findViewById(R.id.txtProfileViews);
        txtProfileConnections = findViewById(R.id.txtProfileConnections);
        imgAddConnection = findViewById(R.id.imgAddConnectionProfile);
        imgConnectionAdded = findViewById(R.id.imgConnectionAddedProfile);
        profileImage = findViewById(R.id.imgProfileImage);
        viewContributions = findViewById(R.id.btnProfileViewContributions);

        populateUsersData();

        dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which)
                {
                    case DialogInterface.BUTTON_POSITIVE: {
                        addConnection(session.getUserId(), user.id);
                        break;
                    }
                    case DialogInterface.BUTTON_NEGATIVE: {
                        dialog.dismiss();
                        break;
                    }
                    default:{
                        dialog.dismiss();
                    }
                }
            }
        };
        viewContributions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchPostsService(UserProfileActivity.this)
                        .execute();
            }
        });
    }

    public void onClickAddConnection(View v) {
        promptAddConnection();
    }

    private void addConnection(String myId, String toAddConnectionId) {
        new RequestConnectionService(this)
                .execute(myId, toAddConnectionId);
    }

    private void promptAddConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog = builder.setMessage("Add " + user.username + " as a connection?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).create();
        alertDialog.show();
    }

    private void populateUsersData() {

        String id = getIntent().getStringExtra("userId");
        new FetchUserService(this).execute(id);
        new FetchMyPostsViewsAndLIkes(
                UserProfileActivity.this, user
        ).execute(id);
    }

    @Override
    public void onFetchCompleted(User user) {
        this.user = user;

        populateProfile();
    }

    private void populateProfile() {
        txtProfileName.setText(user.username);
        txtProfileLocation.setText(user.motherland);
        txtProfileConnections.setText(
                (user.connections == null || user.connections.size() == 0) ?
                        "0" : Integer.toString(user.connections.size())
        );
        txtProfilePosts.setText(
                (user.posts == null || user.posts.size() ==0) ?
                        "0" : Integer.toString(user.posts.size())
         );



        txtProfileLevel.setText(user.badge);

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(R.color.darkerBlueGreen)
                .borderWidthDp(1)
                .cornerRadiusDp(30)
                .oval(false).build();

        Picasso.get()
                .load(user.imageUrl)
                .fit()
                .transform(transformation)
                .into(profileImage);
    }

    @Override
    public void OnRequestedConnectionService(boolean success) {
        if(success) {
            imgAddConnection.setVisibility(View.GONE);
            imgConnectionAdded.setVisibility(View.VISIBLE);
            Toast.makeText(
                    getApplicationContext(),
                    "Successfully sent connection request to " + user.username + "!",
                    Toast.LENGTH_SHORT
            ).show();
        }
        else {
            Toast.makeText(
                    getApplicationContext(),
                    "Couldn't add user. Try again",
                    Toast.LENGTH_SHORT
            ).show();
            alertDialog.dismiss();
        }
    }

    @Override
    public void onFetchCompleted(ArrayList<Post> posts) {
        contributionsPopup = new ContributionsPopup(UserProfileActivity.this,
                R.style.Theme_Dialog,
                posts,
                UserProfileActivity.this);
        //contributionsPopup.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(contributionsPopup.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        contributionsPopup.show();
        contributionsPopup.getWindow().setAttributes(lp);
    }

    @Override
    public void OnContributionsClose() {
        this.contributionsPopup.dismiss();
    }

    @Override
    public void OnFetchMyPostsViewsAndLikesComplted(int views, int likes, User user) {
        int score = ContributionScoreCalc.calculateScore(likes,views,user.posts.size());
        txtProfileLikes.setText(Integer.toString(likes));
        txtProfileViews.setText(Integer.toString(views));
        txtProfileContributionScore.setText(Integer.toString(score));
    }
}
