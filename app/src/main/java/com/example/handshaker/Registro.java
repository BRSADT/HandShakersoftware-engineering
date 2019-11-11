package com.example.handshaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Registro extends AppCompatActivity {
    Button btnGoogle,enviar;
    SignInButton goo;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    public String cuenta,pass;
    public EditText txtCuenta,txtContra;
    private static final String TAG = "GoogleActivity";

    private static final int RC_SIGN_IN = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        enviar= (Button)findViewById(R.id.btnEnviar);
        txtCuenta= (EditText) findViewById(R.id.txtCuenta);

        txtContra= (EditText) findViewById(R.id.txtContra);
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
cuenta=txtCuenta.getText().toString();
pass=txtContra.getText().toString();
                Registrar();


            }

        });

              mAuth = FirebaseAuth.getInstance();
    /*   btnGoogle.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

           }

       });
      */


    }
    public void Registrar(){
        mAuth.createUserWithEmailAndPassword(cuenta, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(),"Se ha registrado" , Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Registro.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });


    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();

        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN );

    }
  /*  @Override

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);



        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                // Google Sign In was successful, authenticate with Firebase

                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {

                // Google Sign In failed, update UI appropriately

                Log.w(TAG, "Google sign in failed", e);

                // [START_EXCLUDE]



                // [END_EXCLUDE]

            }

        }

    }*/
/*
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        // [START_EXCLUDE silent]

   //     showProgressDialog();

        // [END_EXCLUDE]



        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information

                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = mAuth.getCurrentUser();



                        } else {

                            // If sign in fails, display a message to the user.

                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                      //      Snackbar.make(findViewById(R.id.registro), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();

                          //  updateUI(null);

                        }



                        // [START_EXCLUDE]

//                        hideProgressDialog();

                        // [END_EXCLUDE]

                    }

                });

    }*/
    @Override

    public void onStart() {

        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.

//        FirebaseUser currentUser = mAuth.getCurrentUser();
  //      GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
    }





}
