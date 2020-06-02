package com.example.florify.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.florify.Session;
import com.example.florify.db.DBInstance;
import com.example.florify.helpers.MapResolver;
import com.example.florify.helpers.NotificationHelper;
import com.example.florify.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BackgroundService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    public Session session;
    public MapResolver mapResolver;
    public NotificationManagerCompat notificationManagerCompat;
    public NotificationHelper notificationHelper;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mapResolver = new MapResolver(context);
        final Map<String, Object> newLocation = new HashMap();
        session = new Session(context);

        notificationHelper = new NotificationHelper(context);

        notificationManagerCompat = NotificationManagerCompat.from(context);

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                Location location = mapResolver.getLastKnownLocation();
                GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                newLocation.put("location", geoPoint);
                DBInstance.getCollection("users")
                        .document(session.getUserId())
                        .update(newLocation)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {

                                }
                                else {
                                    Toast.makeText(context,
                                            "Update failed in DB",
                                            Toast.LENGTH_SHORT).show();
                                }
                                handler.postDelayed(runnable, 10000);
                            }
                        });
            }
        };

        handler.postDelayed(runnable, 10000);

        DBInstance.getCollection("users").whereEqualTo("id", session.getUserId())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e != null) {
                            Log.w("Background Service", "Listen failed.", e);
                            return;
                        }
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            User user = doc.toObject(User.class);
                            if(user.newConnectionRequests != null && user.newConnectionRequests.size() > 0) {

                                for(String userUid : user.newConnectionRequests) {
                                    getConnectionRequests(userUid);

                                    moveFromNewToOldConnectionRequests(userUid);
                                }
                            }
                        }
                    }
                });
    }

    private void sendNotificationOnChannel(User user) {
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification("New connection request!",
                user.username + " has added you as a connection! Add back?", user.id);
        notificationHelper.getManager().notify(1, nb.build());
    }

    private void getConnectionRequests(String userUid) {
        DBInstance
                .getCollection("users")
                .document(userUid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot result = task.getResult();
                            User connectionRequest = result.toObject(User.class);
                            sendNotificationOnChannel(connectionRequest);
                        }
                    }
                });
    }

    private void moveFromNewToOldConnectionRequests(final String userUid) {
        DBInstance.getCollection("users")
                .document(session.getUserId())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot result = task.getResult();
                            User user = result.toObject(User.class);
                            ArrayList<String >newConnectionRequests = user.newConnectionRequests;
                            ArrayList<String >oldConnectionRequests = user.oldConnectionRequests;

                            newConnectionRequests.remove(userUid);
                            oldConnectionRequests.add(userUid);


                            Map<String,Object> userMap = new HashMap<>();
                            userMap.put("newConnectionRequests", newConnectionRequests);
                            userMap.put("oldConnectionRequests", oldConnectionRequests);

                            //Map

                            DBInstance.getCollection("users")
                                    .document(session.getUserId())
                                    .update(userMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                Log.d("Background service",
                                                        "successfully udpated users old and new connection requests");
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }
}
