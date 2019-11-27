package com.example.handshaker.ui.Mensaje;

import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SendFragment extends Fragment {
    String sel="";
    Button btnEnviarMensaje;
    private SendViewModel sendViewModel;
    private FirebaseAuth mAuth;
    EditText mensaje;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
                ViewModelProviders.of(this).get(SendViewModel.class);
        View Mensaje = inflater.inflate(R.layout.fragment_send, container, false);
        mAuth = FirebaseAuth.getInstance();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            sel = bundle.getString("IdTrabajador");
            Toast.makeText(this.getContext(),sel , Toast.LENGTH_SHORT).show();

        }
        btnEnviarMensaje= Mensaje.findViewById(R.id.btnEnviarMSN);
mensaje= Mensaje.findViewById(R.id.txtMensaje);



        btnEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();

                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("destinatario",sel);
                hashMap.put("emisor",mAuth.getUid());
                hashMap.put("mensaje",mensaje.getText().toString());

                myRef.child("Chats").push().setValue(hashMap);

            }

        });


        return Mensaje;
    }
}