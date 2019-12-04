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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class inicioSesion extends AppCompatActivity {


    private FirebaseAuth mAuth;
    Button enviar;
    EditText txtUsuario;
    EditText txtPass;
    String usuario;
    String contra;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        mAuth = FirebaseAuth.getInstance();
        enviar= (Button)findViewById(R.id.btnEnviar);
        txtUsuario=(EditText)findViewById(R.id.txtUsuario);
        txtPass=(EditText)findViewById(R.id.txtPass);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
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

                            db.collection("Trabajador")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d("datos", document.getId() + " => " + document.getData());
                                                    Log.d("datos", mAuth.getUid());
                                                    if (document.getId().equals(mAuth.getUid())) {
                                                        if(!TienePagoVigente(document.get("PagoVigente").toString())){
                                                            Toast.makeText(getApplicationContext(), "ATENCION: Su pago no es vigente", Toast.LENGTH_LONG).show();
                                                            Toast.makeText(getApplicationContext(), "No aparecerá en las listas", Toast.LENGTH_LONG).show();
                                                            Toast.makeText(getApplicationContext(), "Deslice su dedo desde la parte izquierda", Toast.LENGTH_LONG).show();
                                                            Toast.makeText(getApplicationContext(), "Para poder realizar su pago desde la pestaña pagos", Toast.LENGTH_LONG).show();
                                                        }
                                                        Intent i = new Intent(inicioSesion.this, inicioTrabajador.class);
                                                        startActivity(i);
                                                    }
                                                }
                                            } else {

                                            }
                                        }
                                    });

                            db.collection("Clientes")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d("datos", document.getId() + " => " + document.getData());
                                                    Log.d("datos", mAuth.getUid());
                                                    if (document.getId().equals(mAuth.getUid())) {
                                                        Intent i = new Intent(inicioSesion.this, inicioTrabajador.class);
                                                        startActivity(i);
                                                    }
                                                }
                                            } else {

                                            }
                                        }
                                    });

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

    public boolean TienePagoVigente(String PagoVigente){
        boolean vigencia = false;
        if(PagoVigente.length() > 0){
            Date hoy = Calendar.getInstance().getTime(); //Fecha del día de hoy.

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            try {
                hoy = sdf.parse(sdf.format(hoy));
                Date fechapago = sdf.parse(PagoVigente);
                Log.i("Fecha hoy", "la fecha de hoy es" + hoy.toString());
                Log.i("Fecha pago", "la fecha de pago es" + fechapago.toString());
                if(hoy.equals(fechapago)){
                    vigencia = true;
                }else if(hoy.after(fechapago)){
                    Calendar fecha1mes = Calendar.getInstance();
                    fecha1mes.setTime(fechapago);
                    fecha1mes.add(Calendar.MONTH, 1);
                    if(hoy.before(fecha1mes.getTime()) || hoy.equals(fecha1mes.getTime())){
                        vigencia = true;
                    }
                }else if(hoy.before(fechapago)){
                    Toast.makeText(getApplicationContext(), "No sé como le hiciste para pagar después del día de hoy", Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "pero eso no debería suceder", Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "por favor ponte en contacto con HandShaker", Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "para explicar tu situación", Toast.LENGTH_LONG).show();

                    vigencia = false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Log.v("Exception", e.getLocalizedMessage());
            }
        }
        return vigencia;
    }
}
