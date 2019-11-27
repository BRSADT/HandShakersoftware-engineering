package com.example.handshaker.ui.Mensaje;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.handshaker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;




public class SendFragment extends Fragment {
    String sel="";
    Button btnEnviarMensaje;
    private SendViewModel sendViewModel;
    private FirebaseAuth mAuth;
    EditText mensaje;
    TextView editar;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference referenciaChat = database.getReference();
    View Mensaje;
String texto="";

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
editar=Mensaje.findViewById(R.id.txtMensajes);


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



            }

        });
        referenciaChat.child("Chats").child(mAuth.getUid()).child(sel).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
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
                            men="\t\t\t\t\t"+child.getValue().toString();

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


        /*referenciaChat.child("Chats").child(sel).child(mAuth.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Log.d("User key", child.getKey());// da que es
                    Log.d("User val", child.getValue().toString()); //da el valor
                    if (child.getKey().toString().equals("mensaje")){

                        texto=texto+"/n"+child.getValue().toString();
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


*/
        return Mensaje;
    }
}