package com.example.florify.db.services;

import androidx.annotation.NonNull;

import com.example.florify.db.DBInstance;
import com.example.florify.listeners.OnApproveConnectionRequest;
import com.example.florify.listeners.OnDenyConnectionRequest;
import com.example.florify.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ApproveDenyConnectionRequestService {

    private OnApproveConnectionRequest listenerApprove;
    private OnDenyConnectionRequest listenerDeny;

    public ApproveDenyConnectionRequestService(OnApproveConnectionRequest onApproveConnectionRequest) {
        this.listenerApprove = onApproveConnectionRequest;
        this.listenerDeny = null;
    }

    public ApproveDenyConnectionRequestService(OnDenyConnectionRequest onDenyConnectionRequest) {
        this.listenerDeny = onDenyConnectionRequest;
        this.listenerApprove = null;
    }

    public void ApproveOrDenyConenctionRequest(String myUserId, String connectionUserId) {
        if (listenerApprove != null && listenerDeny == null) {
            RemoveFromOldAndPendingConnections(myUserId, connectionUserId, true);
        } else if (listenerApprove == null && listenerDeny != null) {

            RemoveFromOldAndPendingConnections(myUserId, connectionUserId, false);
        }

    }


    private void RemoveFromOldAndPendingConnections(final String myUserId,
                                                    final String connectionUserId,
                                                    final boolean approve) {

        DBInstance.getCollection("users")
                .document(myUserId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            User user = snapshot.toObject(User.class);
                            ArrayList<String> oldConnections =
                                    user.oldConnectionRequests;
                            ArrayList<String> connections =
                                    user.connections;

                            oldConnections.remove(connectionUserId);

                            if(approve)
                                connections.add(connectionUserId);

                            Map<String, Object> map = new HashMap<>();
                            map.put("oldConnectionRequests", oldConnections);
                            map.put("connections", connections);

                            DBInstance
                                    .getCollection("users")
                                    .document(myUserId)
                                    .update(map)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                removePendingForRequestingUser(
                                                        connectionUserId, myUserId, approve
                                                );
                                            }
                                            else {
                                                if(approve)
                                                    listenerApprove.onApprovedConnectionRequets(false);
                                                else
                                                    listenerDeny.onDeniedConnectionRequest(false);
                                            }
                                        }
                                    });
                        }
                        else {
                            if(approve)
                                listenerApprove.onApprovedConnectionRequets(false);
                            else
                                listenerDeny.onDeniedConnectionRequest(false);
                        }
                    }
                });
    }
    private void removePendingForRequestingUser(final String requestingUserId,
                                                final String connectionToRemoveFromPending,
                                                final boolean approve) {
        DBInstance
                .getCollection("users")
                .document(requestingUserId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot snapshot = task.getResult();
                            User user = snapshot.toObject(User.class);

                            ArrayList<String> pendingRequests = user.pendingConnectionRequests;
                            if(pendingRequests.size() > 0)
                            {
                                pendingRequests.remove(connectionToRemoveFromPending);
                                ArrayList<String> connections = user.connections;

                                if(approve)
                                    connections.add(connectionToRemoveFromPending);

                                Map<String, Object> map = new HashMap<>();
                                map.put("pendingConnectionRequests", pendingRequests);
                                map.put("connections", connections);

                                DBInstance
                                        .getCollection("users")
                                        .document(requestingUserId)
                                        .update(map)
                                        .addOnCompleteListener(
                                                new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()) {
                                                            if(approve)
                                                                listenerApprove.onApprovedConnectionRequets(true);
                                                            else
                                                                listenerDeny.onDeniedConnectionRequest(true);
                                                        }
                                                        else {
                                                            if(approve)
                                                                listenerApprove.onApprovedConnectionRequets(false);
                                                            else
                                                                listenerDeny.onDeniedConnectionRequest(false);
                                                        }
                                                    }
                                                });
                            }

                        }
                        else {
                            if(approve)
                                listenerApprove.onApprovedConnectionRequets(false);
                            else
                                listenerDeny.onDeniedConnectionRequest(false);
                        }
                    }
                });
    }
}

