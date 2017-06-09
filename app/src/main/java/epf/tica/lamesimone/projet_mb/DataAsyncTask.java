package epf.tica.lamesimone.projet_mb;

import android.os.AsyncTask;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paullame on 09/06/2017.
 */


public class DataAsyncTask extends AsyncTask<DatabaseReference, Void, ArrayList<NewsFeedElement>> {

    private NewsFeedFragment fragment;
    ArrayList<NewsFeedElement> elements;
    ListView listView;

    public DataAsyncTask(NewsFeedFragment frag, ListView lview){
        fragment=frag;
        listView=lview;
    }

    ArrayList<NewsFeedElement> tempArray;
    ArrayList<String> array;
    protected ArrayList<NewsFeedElement> doInBackground(DatabaseReference... ref) {
        for(int i=0; i<ref.length;i++) {
            ref[0].addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    tempArray = new ArrayList<>();
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                        array = new ArrayList<>();
                        NewsFeedElement nfe;
                        for (DataSnapshot obj : objSnapshot.getChildren()) {
                            String temp = (String) obj.getValue();
                            array.add(temp);
                        }
                        nfe = generateElements(array.get(4), array.get(0), array.get(1), array.get(2), array.get(3));
                        tempArray.add(nfe);
                    }

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return tempArray;
    }

    protected void onProgressUpdate() {
    }


    protected void onPostExecute(ArrayList<NewsFeedElement> array) {
        elements = tempArray;

    }

    public NewsFeedElement generateElements(String url, String date, String lieu, String nom, String size){
        NewsFeedElement news = new NewsFeedElement(url, date, lieu,nom,size);
        return news;
    }


}