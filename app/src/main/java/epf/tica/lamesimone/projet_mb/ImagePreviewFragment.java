package epf.tica.lamesimone.projet_mb;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;


public class ImagePreviewFragment extends Fragment {


    private static final float LOCATION_REFRESH_DISTANCE = 150;
    private static final long LOCATION_REFRESH_TIME = 5000;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    ImageView imageview;
    TextView textTimeStamp, textLocation, textDate;
    Bitmap imageBitmap;
    String timeStamp;
    String date;
    String location;

    private StorageReference mStorageRef;
    GoogleApiClient mGoogleApiClient;

    LocationManager mLocationManager;

    OkHttpClient client = new OkHttpClient();



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

        mLocationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);


        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                LOCATION_REFRESH_DISTANCE, mLocationListener);

        //System.out.println(mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));

        location = formatLocation(mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude(),
                mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude(),
                mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getAltitude());
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
                dispatchTakePictureIntent();
            }
        });

        Button bouton = (Button) view.findViewById(R.id.button3);
        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload(imageBitmap, timeStamp);
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



    public void uploadFile(StorageReference storageRef, Bitmap bitmap, String path) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference riversRef = storageRef.child("images/" + path + ".jpg");
        UploadTask uploadTask = riversRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Get a URL to the uploaded content
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                System.out.println("upload success");
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

   private String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
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
        String Slatitude = String.format("%.2f", latitude);
        String Slongitude = String.format("%.2f", longitude);
        String Saltitude = String.format("%.2f", altitude);


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

            textLocation.setText(location);


        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
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
