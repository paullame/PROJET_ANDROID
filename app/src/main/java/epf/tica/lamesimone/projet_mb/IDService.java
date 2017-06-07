package epf.tica.lamesimone.projet_mb;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by paullame on 06/06/2017.
 */

public class IDService extends FirebaseInstanceIdService {

    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("token", "Refreshed token " + refreshedToken);

       // sendRegistrationToServer(refreshedToken);
    }
}
