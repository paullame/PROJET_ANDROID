package epf.tica.lamesimone.projet_mb;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;


import static android.app.Activity.RESULT_OK;


public class ImagePreviewFragment extends Fragment {


    private static final float LOCATION_REFRESH_DISTANCE = 0;
    private static final long LOCATION_REFRESH_TIME = 10000;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CHECK_SETTINGS = 1;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    private FusedLocationProviderClient mFusedLocationClient;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=21;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION=22;


    protected void createLocationRequest() {
        final LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_REFRESH_TIME);
        mLocationRequest.setFastestInterval(LOCATION_REFRESH_TIME);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        final LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {

                initFusedLocationWrapper();


            }
        });

        task.addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(getActivity(),
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }

            }
        });
    }

    private void initFusedLocationWrapper() {


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        }

        else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                myLocation = formatLocation(location.getLatitude(), location.getLongitude(), location.getAltitude());
                            } else {
                                Log.d("fail", "impossible de recuperer la position");
                                myLocation="gps non disponible";
                            }
                        }

                    });
        }


    }


    ImageView imageview;
    TextView textTimeStamp, textLocation, textDate;
    Bitmap imageBitmap;
    String timeStamp;
    String date;
    String myLocation;
    String size;
    String downloadString;
    ArrayList<NewsFeedElement> elements;

    private StorageReference mStorageRef;


    public ImagePreviewFragment() {
        // Required empty public constructor
    }


    public static ImagePreviewFragment newInstance() {
        ImagePreviewFragment fragment = new ImagePreviewFragment();
        Bundle args = new Bundle();
/*        args.putString();
        args.putString();*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createLocationRequest();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_preview, container, false);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        imageview = (ImageView) view.findViewById(R.id.imagePreview);

        textTimeStamp = (TextView) view.findViewById(R.id.timestamp);
        textLocation = (TextView) view.findViewById(R.id.location);
        textDate = (TextView) view.findViewById(R.id.textDate);



        Button openCamera = (Button) view.findViewById(R.id.button2);
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initFusedLocationWrapper();
                dispatchTakePictureIntent();
            }
        });

        Button bouton = (Button) view.findViewById(R.id.button3);
        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    upload(imageBitmap, timeStamp);
                }
                catch (NullPointerException e) {
                    Toast.makeText(getActivity(), "oups! il faut d'abord prendre une photo",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public interface OnFragmentInteractionListener {

    }

    private void uploadFile(StorageReference storageRef, Bitmap bitmap, final String path) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        size = String.format("%.2f", (double) data.length / 1000) + "Ko";

        final StorageReference riversRef = storageRef.child("images/" + path + ".jpg");
        UploadTask uploadTask = riversRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Get a URL to the uploaded content
                System.out.println("upload success");
                downloadString = "images/"+path+".jpg";
                sendData();
                new DataAsyncTask().execute("un utilisateur a envoyé une nouvelle photo");
                Toast.makeText(getActivity(), "photo envoyée",
                        Toast.LENGTH_SHORT).show();

            }
        });
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                System.out.println("upload failure");
                Toast.makeText(getActivity(), "échec de l'envoi",
                        Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void sendData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("photos").push();

        myRef.child("url").setValue(downloadString);
        myRef.child("date").setValue(timeStamp);

        if(myLocation==null){
            myRef.child("lieu").setValue("donnée non disponible");
        }
        else {
            myRef.child("lieu").setValue(myLocation);
        }

        myRef.child("nom").setValue(timeStamp);
        myRef.child("taille").setValue(size);
    }


    public void upload(Bitmap bitmap, String path) {
        mStorageRef = FirebaseStorage.getInstance().getReference();
        uploadFile(mStorageRef, bitmap, path);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private String formatLocation(double latitude, double longitude, double altitude) {
        String location;
        String Slatitude = String.format("%.3f", latitude);
        String Slongitude = String.format("%.3f", longitude);
        String Saltitude = String.format("%.3f", altitude);


        location = "latitude: " + Slatitude + " longitude " + Slongitude + " altitude: " + Saltitude;
        return location;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageview.setImageBitmap(imageBitmap);

            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            textTimeStamp.setText(timeStamp);

            date = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.FRANCE).format(new Date());
            textDate.setText(date);
            initFusedLocationWrapper();
            textLocation.setText(myLocation);


        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        Log.d("requestCode", "request code is "+requestCode);


        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
