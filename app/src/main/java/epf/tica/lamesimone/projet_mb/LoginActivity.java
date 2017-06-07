package epf.tica.lamesimone.projet_mb;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private StorageReference mStorageRef;
    Uri path =Uri.parse("android.resource://epf.tica.lamesimone.projet_mb/"+ R.drawable.fox);

    private String userMail;
    private String userUID;

    public static final String EXTRA_MAIL = "com.example.myfirstapp.MAIL";
    public static final String EXTRA_UID = "com.example.myfirstapp.UID";


    String imageUri = path.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




        final Button button2 = (Button) findViewById(R.id.buttonSignUp);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signUp();
            }
        });


        mAuth= FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("WELELE", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("WALELAR", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
try {

} catch (NullPointerException e) {

}

    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void createAccount(String email, String password) throws IllegalArgumentException{
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("creation d'utilisateur", "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if(task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "le compte a correctement été créé",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful() ) {
                            Toast.makeText(LoginActivity.this, "le compte n'a pas pu être créé",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void signIn(String email, String password) throws IllegalArgumentException{

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("authentification", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if(task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            userMail = mAuth.getCurrentUser().getEmail();
                            userUID = mAuth.getCurrentUser().getUid();
                            intent.putExtra(EXTRA_MAIL,userMail);
                            intent.putExtra(EXTRA_UID,userUID);
                            startActivity(intent);
                        }
                        if (!task.isSuccessful()) {
                            Log.w("authentifaction echec", "signInWithEmail:failed", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void connexion(View view) {

        EditText idField = (EditText) findViewById(R.id.id_field);
        String userId = idField.getText().toString();

        EditText pdField = (EditText) findViewById(R.id.password_field);
        String userPd = pdField.getText().toString();
        try {
            signIn(userId, userPd);
        } catch(IllegalArgumentException e) {
            Toast.makeText(LoginActivity.this, R.string.empty_field,
                    Toast.LENGTH_SHORT).show();
        }



    }

    public void signUp() {

        EditText idField = (EditText) findViewById(R.id.id_field);
        String userId = idField.getText().toString();
        System.out.println(userId);

        EditText pdField = (EditText) findViewById(R.id.password_field);
        String userPd = pdField.getText().toString();
        System.out.println(userPd);
        try {

            createAccount(userId, userPd);
        } catch(IllegalArgumentException e) {
            Toast.makeText(LoginActivity.this,R.string.empty_field,
                    Toast.LENGTH_SHORT).show();
        }

    }




}