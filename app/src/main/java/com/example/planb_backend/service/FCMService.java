package com.example.planb_backend.service;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.planb_backend.User;
import com.example.planb_backend.utils.NotificationID;
import com.example.planb_frontend.R;
import com.example.planb_frontend.StudentConnectionActivity;
import com.example.planb_frontend.StudentPageActivity;
import com.example.planb_frontend.StudentRegisterActivity;
import com.example.planb_frontend.TutorPageActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;
import java.util.Objects;

import static com.example.planb_frontend.StudentRegisterActivity.USERS_TABLE_KEY;

public class FCMService extends FirebaseMessagingService {

//    private final FirebaseMessaging firebaseMessagingInstance;

    public static final String FCM_TAG = "FCM_SERVICE";

    /**
     * Called if FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve
     * the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(FCM_TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
//        sendRegistrationToServer(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(FCM_TAG, "From: " + remoteMessage.getFrom());

        Map<String, String> data = remoteMessage.getData();
        // Check if message contains a data payload.
        if (data.size() > 0) {
            Log.d(FCM_TAG, "Message data payload: " + remoteMessage.getData());
        }
        else {
            Log.e(FCM_TAG, "Message data lost. Check connection.");
            return;
        }

//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d(FCM_TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || data.get("uid") == null) return;
        if (!Objects.requireNonNull(data.get("uid")).equals(user.getUid())) return;

        String userId = user.getUid();
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();

        DocumentReference documentReference = fStore.collection(USERS_TABLE_KEY).document(userId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user;
                if (documentSnapshot.exists()) {
                    user = documentSnapshot.toObject(User.class);
                    assert user != null;
                    user.setEmail(fAuth.getCurrentUser().getEmail());
                    user.setId(userId);
                    Intent resultIntent = new Intent(getApplicationContext(), StudentConnectionActivity.class);
                    resultIntent.putExtra(StudentRegisterActivity.GET_USER_KEY, user);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                    stackBuilder.addNextIntentWithParentStack(resultIntent);
                    // Get the PendingIntent containing the entire back stack
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "plan-b-fcm")
                            .setSmallIcon(R.drawable.logo)
                            .setContentTitle("Ticket accepted")
                            .setContentText("Your ticket has been accepted by: " + data.get("name"))
                            .setContentIntent(resultPendingIntent)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                    // notificationId is a unique int for each notification that you must define
                    notificationManager.notify(NotificationID.getID(), builder.build());
                } else {
//                    Toast.makeText(getApplicationContext(), "error occurred", Toast.LENGTH_LONG).show();
                    Log.e(FCM_TAG, "Unexpected error occurred.");
                }
            }
        });
    }
}
