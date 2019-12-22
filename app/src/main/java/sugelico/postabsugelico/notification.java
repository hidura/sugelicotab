package sugelico.postabsugelico;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by hidura on 9/22/2017.
 */

public class notification extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Toast.makeText(getApplicationContext(), remoteMessage.getFrom(), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), remoteMessage.getNotification().getBody(),
                Toast.LENGTH_LONG).show();
    }
}
