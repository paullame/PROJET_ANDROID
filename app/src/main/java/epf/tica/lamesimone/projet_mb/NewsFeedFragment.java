package epf.tica.lamesimone.projet_mb;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFeedFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public ListView getListView() {
        return listView;
    }

    private ListView listView;
    private OkHttpClient client = new OkHttpClient();
    private ArrayList<String> arrayDate=null;
    private ArrayList<String> arrayLieu=null;
    private ArrayList<String> arrayNom=null;
    private ArrayList<String> arrayTaille=null;
    private ArrayList<String> arrayUrl=null;
    private ArrayList<String> array=null;
    boolean dateOk =false;
    boolean lieuOk =false;
    boolean nomOk =false;
    boolean tailleOk =false;
    boolean urlOk =false;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference("photos");
    private ValueEventListener datalistener;
    private Query mostRecent;




    public NewsFeedFragment() {
        // Required empty public constructor
    }
    public static NewsFeedFragment newInstance(String param1, String param2) {
        NewsFeedFragment fragment = new NewsFeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);

        listView = (ListView) view.findViewById(R.id.photoFeed);




        datalistener= new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<NewsFeedElement> tempArray = new ArrayList<>();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    array = new ArrayList<>();
                    NewsFeedElement nfe;
                    for (DataSnapshot obj : objSnapshot.getChildren()) {
                        String temp = (String) obj.getValue();
                        array.add(temp);
                    }
                    SimpleDateFormat parseDate = new SimpleDateFormat("yyyyMMdd_hhmmss");
                    SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                    Date date=null;
                    try {
                        date = parseDate.parse(array.get(0));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    nfe = generateElements(array.get(4),format.format(date), array.get(1), array.get(2), array.get(3));
                    tempArray.add(nfe);
                    Collections.reverse(tempArray);
                }
                NewsFeedAdapter adapter = new NewsFeedAdapter(getContext(), tempArray);
                listView.setAdapter(adapter);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mostRecent = myRef.orderByKey();
      mostRecent.addValueEventListener(datalistener);



        return view;
    }


    public NewsFeedElement generateElements(String url, String date, String lieu, String nom, String size){
        NewsFeedElement news = new NewsFeedElement(url, date, lieu,nom,size);
        return news;
    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
/*        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
       // myRef.removeEventListener(datalistener);
    }

    @Override
    public void onPause(){
        super.onPause();
        mostRecent.removeEventListener(datalistener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mostRecent.removeEventListener(datalistener);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}
