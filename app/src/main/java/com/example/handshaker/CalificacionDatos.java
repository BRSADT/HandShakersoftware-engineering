package com.example.handshaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handshaker.ui.Mensaje.SendFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CalificacionDatos extends AppCompatActivity  implements Serializable {
    String sel="";
    private FirebaseAuth mAuth;
    ImageView img;
    TextView txtNombre,txtOficio ;
    View TrabajadorView;
    StorageReference storageRef;
    FirebaseStorage storage ;
    StorageReference referencia ;
    RatingBar rb,rbanterior;
    private  FirebaseFirestore db;
    Button enviar;
    int personasCal;
    float PuntajeT,puntaje;
    EditText txtComentario;
float calificacion=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificacion_datos);
        rb=(RatingBar) findViewById(R.id.ratingBar2);

        img=(ImageView) findViewById(R.id.img);
        txtNombre=(TextView) findViewById(R.id.txtNombre);
        txtOficio=(TextView) findViewById(R.id.txtOficio);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        enviar=(Button) findViewById(R.id.btnEnv);
        txtComentario=(EditText) findViewById(R.id.txtComentario);
        mAuth = FirebaseAuth.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        db = FirebaseFirestore.getInstance();

        sel=(String)getIntent().getSerializableExtra("IdTrabajador");

        mAuth = FirebaseAuth.getInstance();


        referencia = storage.getReference().child(sel).child("profile.jpg");


        GlideApp.with(this )
                .load(referencia)
                .into(img);


        DocumentReference docRef = db.collection("Trabajador").document(sel);
        Source source = Source.CACHE;
        docRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
                    txtNombre.setText(document.get("Nombre").toString()+" "+document.get("Apellido").toString());
                    txtOficio.setText(document.get("Oficio").toString());
                    personasCal=Integer.parseInt(document.get("NpersonasCal").toString());
                    PuntajeT=Float.parseFloat(document.get("Puntaje").toString());

                    Log.d("Datos-info del usuario", "Cached document data: " + document.getData());
                } else {
                    Log.d("Datos-Error", "Cached get failed: ", task.getException());
                }
            }
        });

enviar.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
         calificacion=rb.getRating()*2*10;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("mensaje",txtComentario.getText().toString() );
        hashMap.put("calificacion",String.valueOf(calificacion));
        hashMap.put("usuario",String.valueOf(mAuth.getUid()));

        myRef.child("Calificaciones").child(sel).child(mAuth.getUid()).push().setValue(hashMap);


//cambiar a calificado

        Map<String, Object> datos = new HashMap<>();
        datos.put("calificado", "calificado");
        datos.put("estado", "Solicitar");
        db.collection("Trabajador").document(sel).collection("Clientes").document(mAuth.getUid()).set(datos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                    }});
            personasCal++;
         puntaje=PuntajeT+rb.getRating();
        Map<String, Object> datos2 = new HashMap<>();
        datos2.put("NpersonasCal",personasCal );
        datos2.put("Puntaje",personasCal);
        db.collection("Trabajador").document(sel).update(datos2)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent i = new Intent(getApplicationContext(), inicioTrabajador.class);
                        startActivity(i);

                    }});


    }

});





    }
}
