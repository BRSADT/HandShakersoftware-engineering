package com.example.handshaker.ui.Mensaje;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.handshaker.GlideApp;
import com.example.handshaker.R;
import com.example.handshaker.ui.TrabajadorSeleccionado.ToolsViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;




public class SendFragment extends Fragment {
    String sel="";
    Button btnEnviarMensaje;
    private SendViewModel sendViewModel;
    private FirebaseAuth mAuth;
    EditText mensaje;
    TextView editar,NombreTChat,OficioTchat;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference referenciaChat = database.getReference();
    View Mensaje;
    String texto="";
    ScrollView ScrollMensajes;
    ImageView fotoTChat;


    private ToolsViewModel toolsViewModel;
    StorageReference storageRef;
    FirebaseStorage storage ;
    StorageReference referencia ;

    private  FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
                ViewModelProviders.of(this).get(SendViewModel.class);
        Mensaje = inflater.inflate(R.layout.fragment_send, container, false);
        mAuth = FirebaseAuth.getInstance();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            sel = bundle.getString("IdTrabajador");
            Toast.makeText(this.getContext(),sel , Toast.LENGTH_SHORT).show();

        }
        btnEnviarMensaje= Mensaje.findViewById(R.id.btnEnviarMSN);
        mensaje= Mensaje.findViewById(R.id.txtMensaje);
        ScrollMensajes=Mensaje.findViewById(R.id.ScrollMensajes);
        editar=Mensaje.findViewById(R.id.txtMensajes);
        fotoTChat=Mensaje.findViewById(R.id.fotoTChat);
        NombreTChat=Mensaje.findViewById(R.id.NombreTChat);
        OficioTchat=Mensaje.findViewById(R.id.OficioTchat);

        //poner foto y nombre del destinatario

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        db = FirebaseFirestore.getInstance();


        referencia = storage.getReference().child(sel).child("profile.jpg");


        GlideApp.with(this )
                .load(referencia)
                .into(fotoTChat);
        DocumentReference docRef = db.collection("Trabajador").document(sel);
        Source source = Source.CACHE;
        docRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
                    NombreTChat.setText(document.get("Nombre").toString()+" "+document.get("Apellido").toString());
                    OficioTchat.setText(document.get("Oficio").toString());
                    Log.d("Datos-info del usuario", "Cached document data: " + document.getData());
                } else {
                    Log.d("Datos-Error", "Cached get failed: ", task.getException());
                }
            }
        });





        //Enviar mensaje
        btnEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();

                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("destinatario",sel);
                hashMap.put("emisor",mAuth.getUid());
                hashMap.put("mensaje",mensaje.getText().toString());

                myRef.child("Chats").child(sel).child(mAuth.getUid()).push().setValue(hashMap);
                myRef.child("Chats").child(mAuth.getUid()).child(sel).push().setValue(hashMap);
                mensaje.setText("");


            }

        });
        //Si se ha enviado un mensaje
        referenciaChat.child("Chats").child(mAuth.getUid()).child(sel).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ScrollMensajes.fullScroll(1);
                    String men="";
                String Quien="";
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Log.d("User key", child.getKey());// da que es
                    Log.d("User val", child.getValue().toString()); //da el valor

                    if (child.getKey().toString().equals("emisor")) {
                        Quien=child.getValue().toString();


                    }
                    if (child.getKey().toString().equals("mensaje")){

                        if (Quien.equals(sel.toString())){
                            men="\t\t\t\t\t\t\t\t\t\t\t\t\t+" +
                                    ""+child.getValue().toString();

                        }else
                    {
                        men=child.getValue().toString();

                    }
                        texto=texto+"\n"+men;

                        editar.setText(texto);
                    }


                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return Mensaje;
    }

}