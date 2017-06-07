package epf.tica.lamesimone.projet_mb;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


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

    ListView listView;

    private String[] prenoms = new String[]{
            "Antoine", "Benoit", "Cyril", "David", "Eloise", "Florent",
            "Gerard", "Hugo", "Ingrid", "Jonathan", "Kevin", "Logan",
            "Mathieu", "Noemie", "Olivia", "Philippe", "Quentin", "Romain",
            "Sophie", "Tristan", "Ulric", "Vincent", "Willy", "Xavier",
            "Yann", "Zoé"
    };





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

        List<NewsFeedElement> elements =generateElements();
        NewsFeedAdapter adapter = new NewsFeedAdapter(getContext(), elements);
        listView.setAdapter(adapter);

        View cellule;


        return view;
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
