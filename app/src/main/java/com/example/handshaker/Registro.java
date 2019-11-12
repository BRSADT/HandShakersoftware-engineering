package com.example.handshaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registro extends AppCompatActivity {
    Button btnGoogle,enviar;
    SignInButton goo;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    public String cuenta,pass;
    public EditText txtCuenta,txtContra;
    public RadioGroup tipo;
    public RadioButton cliente, trabajador;
    private static final String TAG = "GoogleActivity";

    private static final int RC_SIGN_IN = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        enviar= (Button)findViewById(R.id.btnEnviar);
        txtCuenta= (EditText) findViewById(R.id.txtCuenta);
        tipo=(RadioGroup) findViewById(R.id.Tipo);
        cliente=(RadioButton) findViewById(R.id.soyCliente);
        trabajador=(RadioButton) findViewById(R.id.soyTrabajador);
        txtContra= (EditText) findViewById(R.id.txtPass);
        mAuth = FirebaseAuth.getInstance();
        tipo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                enviar.setEnabled(true);
            }
        });

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        cuenta=txtCuenta.getText().toString();
        pass=txtContra.getText().toString();
                Registrar();
            }

        });




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
                            int id=tipo.getCheckedRadioButtonId();
                            int idCliente=cliente.getId();

                            if (id==cliente.getId()){
                                //se va a toma datos cliente
                                Intent i=new Intent(Registro.this,DatosClientes.class);
                                startActivity(i);
                            }
                            else{
                                if (id==trabajador.getId()){
                                    //se va a toma datos trabajador
                                    Intent i=new Intent(Registro.this,DatosTrabajadores.class);
                                    startActivity(i);
                                }

                            }


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Registro.this, "Error con la cuenta a registrar.",
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
