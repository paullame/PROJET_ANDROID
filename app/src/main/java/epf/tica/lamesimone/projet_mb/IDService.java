package epf.tica.lamesimone.projet_mb;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by paullame on 06/06/2017.
 */

public class IDService extends FirebaseInstanceIdService {
public String refreshedToken;
    public void onTokenRefresh() {
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("token", "Refreshed token " + refreshedToken);
        FirebaseMessaging.getInstance().subscribeToTopic("news");

       // sendRegistrationToServer(refreshedToken);
    }
}
