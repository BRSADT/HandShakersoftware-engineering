package com.example.handshaker.ui.libre;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.handshaker.GlideApp;
import com.example.handshaker.R;
import com.example.handshaker.ui.Mensaje.SendFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ShareFragment extends Fragment {
    StorageReference storageRef;
    private FirebaseAuth mAuth;
    private ShareViewModel shareViewModel;
    Button btnEnviarMensaje;
    StorageReference storageReference;

    FirebaseStorage storage ;
    String sel="";
    private  FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareViewModel =
                ViewModelProviders.of(this).get(ShareViewModel.class);
        View MsnTrabajador = inflater.inflate(R.layout.fragment_share, container, false);


            Toast.makeText(this.getContext(),"mensajes" , Toast.LENGTH_SHORT).show();


        mAuth = FirebaseAuth.getInstance();
        // inicializat
        storage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        db = FirebaseFirestore.getInstance();
        db.collection("Trabajador").document(mAuth.getUid()).collection("Clientes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                   if(document  != null &&document .exists()) {

                                    Log.d("mensaje4", document.getId() + " => " + document.getData());

/*

                                lcomentario.add(new LinearLayout(TrabajadorView.getContext()));

                                o.setMargins(0, 0, 0, 10);
                                Parfotos.setMargins(0, 0, 0, 10);
                                Pseparar1.setMargins(0, 0, 0, 10);
                                Pprincipal.setMargins(0, 0, 0, 10);

                                principal.add(new LinearLayout(TrabajadorView.getContext()));
                                principal.get(x).setLayoutParams(o);
                                principal.get(x).setOrientation(LinearLayout.HORIZONTAL);

                                layoutSeparar1.add(new LinearLayout(TrabajadorView.getContext()));
                                layoutSeparar1.get(x).setLayoutParams(Pseparar1);


                                layoutDatos.add(new LinearLayout(TrabajadorView.getContext()));
                                layoutDatos.get(x).setLayoutParams(PDatos);

                                comentarios.add(new TextView(TrabajadorView.getContext()));
                                comentarios.get(x).setText(child.child("mensaje").getValue().toString());


                                layoutSeparar2.add(new LinearLayout(TrabajadorView.getContext()));
                                layoutSeparar2.get(x).setLayoutParams(Pseparar2);

                                layoutFotos.add(new LinearLayout(TrabajadorView.getContext()));
                                layoutFotos.get(x).setLayoutParams(Parfotos);

                                layoutFotos.get(x).setOrientation(LinearLayout.HORIZONTAL);


                                icono.add(new ImageView(TrabajadorView.getContext()));
                                icono.get(x).setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                referencia2 = storage.getReference().child(IDusuarios.get(x)).child("profile.jpg");

                                GlideApp.with(TrabajadorView)
                                        .load(referencia2)
                                        .into(icono.get(x));


                                layoutDatos.get(x).addView(comentarios.get(x));
                                layoutFotos.get(x).addView(icono.get(x));
                                principal.get(x).addView(layoutSeparar1.get(x));

                                principal.get(x).addView(layoutFotos.get(x));
                                principal.get(x).addView(layoutSeparar2.get(x));
                                principal.get(x).addView(layoutDatos.get(x));


                                x++;

*/
                                }
                            else{


                                }
                            }


                         /*   for (int i=0;i<x;i++){
                                Log.d("mensaje", String.valueOf(i) );

                                ll.addView(layoutSeparar.get(i));
                            }

*/




                        } else {
                            Log.d("mensaje", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return MsnTrabajador;
    }
}