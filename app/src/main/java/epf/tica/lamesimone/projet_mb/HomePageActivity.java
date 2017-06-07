package epf.tica.lamesimone.projet_mb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class HomePageActivity extends AppCompatActivity {

    private StorageReference mStorageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        downloadFile(mStorageRef, imageView);

    }




    public void downloadFile(StorageReference imageRef, final ImageView imgview) {
        final long ONE_MEGABYTE = 1024 * 1024;
        imageRef.child("images/fox.jpg").getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                displayImage(bytes,imgview);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    public void displayImage(byte[] bytes, ImageView imgview) {
        Bitmap img= BitmapFactory.decodeByteArray(bytes,0, bytes.length);
        imgview.setImageBitmap(img);

    }

}
