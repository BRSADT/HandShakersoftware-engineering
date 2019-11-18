package com.example.handshaker.ui.tools;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.handshaker.GlideApp;
import com.example.handshaker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ToolsFragment extends Fragment {

    private ToolsViewModel toolsViewModel;
    String sel="";
    private FirebaseAuth mAuth;
    ImageView fotoTrabajador;
    TextView nombre,apellido,oficio,celular;
    View TrabajadorView;
    StorageReference storageRef;
    FirebaseStorage storage ;
    StorageReference referencia ;

    private  FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        View TrabajadorView = inflater.inflate(R.layout.fragment_tools, container, false);
        fotoTrabajador=(ImageView)TrabajadorView.findViewById(R.id.fotoPerfil);
        nombre=(TextView) TrabajadorView.findViewById(R.id.Nombre);
        oficio=(TextView) TrabajadorView.findViewById(R.id.Oficio);

       /* final TextView textView = root.findViewById(R.id.text_tools);
        toolsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

*/


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            sel = bundle.getString("IdTrabajador");
            Toast.makeText(this.getContext(),sel , Toast.LENGTH_SHORT).show();

        }

//cambiar datos

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        db = FirebaseFirestore.getInstance();


        referencia = storage.getReference().child(sel).child("profile.jpg");


        GlideApp.with(this )
                .load(referencia)
                .into(fotoTrabajador);
        DocumentReference docRef = db.collection("Trabajador").document(sel);
        Source source = Source.CACHE;
        docRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
                        nombre.setText(document.get("Nombre").toString()+" "+document.get("Apellido").toString());
                        oficio.setText(document.get("Oficio").toString());
                    Log.d("Datos-info del usuario", "Cached document data: " + document.getData());
                } else {
                    Log.d("Datos-Error", "Cached get failed: ", task.getException());
                }
            }
        });




        return TrabajadorView;
}}