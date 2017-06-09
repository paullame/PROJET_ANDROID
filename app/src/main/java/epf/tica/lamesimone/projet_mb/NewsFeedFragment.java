package epf.tica.lamesimone.projet_mb;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
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
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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

    private ListView listView;
    private OkHttpClient client = new OkHttpClient();
    private ArrayList<String> array=null;




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




        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("photos");

        // Read from the database
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                List<NewsFeedElement> tempArray = new ArrayList<NewsFeedElement>();

                array = new ArrayList<String>(10);
                NewsFeedElement nfe;
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {

                    String url;
                    String date;
                    String lieu;
                    String nom;
                    String taille;
                    String temp = (String) objSnapshot.getValue();
                    array.add(temp);
                    for (DataSnapshot obj : objSnapshot.getChildren()) {
                        //System.out.println(obj.getValue());


                    }

                }
                nfe = generateElements(array.get(4), array.get(0), array.get(1), array.get(2), array.get(3));
                tempArray.add(nfe);
                final List<NewsFeedElement> elements = tempArray;
                NewsFeedAdapter adapter = new NewsFeedAdapter(getContext(), elements);
                listView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        return view;
    }

    private void run(String url) throws IOException, JSONException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        String jsonData = response.body().string();
        JSONObject Jobject = new JSONObject(jsonData);
        JSONArray Jarray = Jobject.getJSONArray("");

        for (int i = 0; i < Jarray.length(); i++) {
            JSONObject object     = Jarray.getJSONObject(i);
        }
    }

    private List<NewsFeedElement> generateElements(){
        List<NewsFeedElement> elements = new ArrayList<NewsFeedElement>();
        elements.add(new NewsFeedElement("https://firebasestorage.googleapis.com/v0/b/projet-mb.appspot.com/o/images%2F20170607_125912.jpg?alt=media&token=f31ce5af-7ab7-48e5-b0d6-681f391dc800", "01/01/01", "1 rue A","lol1","100"));
        elements.add(new NewsFeedElement("https://firebasestorage.googleapis.com/v0/b/projet-mb.appspot.com/o/images%2F20170607_191506.jpg?alt=media&token=b3de5b1e-43df-4cd6-8b53-3afb074126d8", "02/01/01", "2 rue A","lol2","200"));
        elements.add(new NewsFeedElement("https://firebasestorage.googleapis.com/v0/b/projet-mb.appspot.com/o/images%2F20170607_191506.jpg?alt=media&token=b3de5b1e-43df-4cd6-8b53-3afb074126d8", "03/01/01", "3 rue A","lol3","300"));
        elements.add(new NewsFeedElement("https://firebasestorage.googleapis.com/v0/b/projet-mb.appspot.com/o/images%2F20170607_191506.jpg?alt=media&token=b3de5b1e-43df-4cd6-8b53-3afb074126d8", "04/01/01", "4 rue A","lol4","400"));
        elements.add(new NewsFeedElement("https://firebasestorage.googleapis.com/v0/b/projet-mb.appspot.com/o/images%2F20170607_191506.jpg?alt=media&token=b3de5b1e-43df-4cd6-8b53-3afb074126d8", "05/01/01", "5 rue A","lol5","500"));
        return elements;
    }

    private NewsFeedElement generateElements(String url, String date, String lieu, String nom, String size){
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
        mListener = null;
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
