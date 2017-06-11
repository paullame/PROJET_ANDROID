package epf.tica.lamesimone.projet_mb;

import android.os.AsyncTask;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by paullame on 09/06/2017.
 */


public class DataAsyncTask extends AsyncTask<String, Void, String> {


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();




    protected String doInBackground(String... message) {
        String result="ERROR, NO RETURN MESSAGE";
        for(int i=0; i<message.length;i++)
        try {
           result= post("https://fcm.googleapis.com/fcm/send", jsonBuilder(message[i]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    protected void onProgressUpdate() {
    }


    protected void onPostExecute(String result) {
        System.out.println(result);
    }



    private String jsonBuilder(String message){
        String json="{\"to\" : \"/topics/news\", \"data\": { \"message\": \""+message+"\",}}";

        return json;
    }

    private String post(String url, String json) throws IOException {

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-type","application/json")
                .addHeader("Authorization","key=AAAAC33V2lE:APA91bHnNfwqvGbs88HROPMyOKW7PD4V_K2XDHd3N8obo-NXGGd9_qUG4xq8i3hQRV0737niXZc8Mz47t76KCenIaxamPGx3PWRHVoczgb6kkx7saMTwFtbV_7XDhUVgkJ64HZyalheR")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }


}