package com.example.handshaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class inicioSesion extends AppCompatActivity {


    private FirebaseAuth mAuth;
    Button enviar;
    EditText txtUsuario;
    EditText txtPass;
    String usuario;
    String contra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        mAuth = FirebaseAuth.getInstance();
        enviar= (Button)findViewById(R.id.btnEnviar);
        txtUsuario=(EditText)findViewById(R.id.txtUsuario);
        txtPass=(EditText)findViewById(R.id.txtPass);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               signIn();
            }

        });}


    public void signIn(){
        contra=txtPass.getText().toString();
        usuario=txtUsuario.getText().toString();



        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(usuario, contra)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(),"Ha ingresado" , Toast.LENGTH_SHORT).show();

                            Intent i=new Intent(inicioSesion.this,prueba.class);
                            startActivity(i);
                        } else {
                            Toast .makeText(getApplicationContext(),"No es correcto email" , Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {

                        }

                    }
                });
        // [END sign_in_with_email]



    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
    }
}
