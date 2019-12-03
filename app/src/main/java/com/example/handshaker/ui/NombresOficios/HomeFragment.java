package com.example.handshaker.ui.NombresOficios;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.handshaker.GlideApp;
import com.example.handshaker.R;
import com.example.handshaker.ui.seleccionTrabajadorOficio.GalleryFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    ArrayList<LinearLayout> layoutSeparar=new ArrayList<LinearLayout>();
    ArrayList<ImageView> icono=new ArrayList<ImageView>();
    private  FirebaseFirestore db;
    private HomeViewModel homeViewModel;
    View homeView;
    int x=0;
    StorageReference storageRef;
    private FirebaseAuth mAuth;
    FirebaseStorage storage ;
    StorageReference referencia ;
    ArrayList<StorageReference> refe=new ArrayList<StorageReference>();
    LinearLayout ll;
    StorageReference storageReference;
    LinearLayout.LayoutParams o;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
         homeView = inflater.inflate(R.layout.fragment_home, container, false);
        //FirebaseStorage storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
         // inicializat
     storage = FirebaseStorage.getInstance();
       storageReference = FirebaseStorage.getInstance().getReference();



         ll = (LinearLayout)homeView.findViewById(R.id.Scroll);
         o = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);

         //CONSULTA OFICIOS
         db = FirebaseFirestore.getInstance();
//HOLA


       db.collection("Oficios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("mensaje", document.getId() + " => " + document.getData());
                                Log.d("mensaje", document.get("nombreOficio").toString() + " => " + document.getData());
                                layoutSeparar.add(new LinearLayout(homeView.getContext()));
                                o.setMargins(0,0,0,10);
                                layoutSeparar.get(x).setLayoutParams(o);
                                layoutSeparar.get(x).setOrientation(LinearLayout.HORIZONTAL);



                                String re=document.get("nombreOficio").toString()+".png";

                                FirebaseStorage storage = FirebaseStorage.getInstance();

                                referencia = storage.getReference().child("fotosOficios").child(re);




                                icono.add(new ImageView(homeView.getContext()));
                                icono.get(x).setLayoutParams( new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,1.0f));
                                icono.get(x).setTag(document.get("nombreOficio").toString());
                                GlideApp.with(homeView )
                                        .load(referencia)
                                        .into(icono.get(x));




                                icono.get(x).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
//                                        Toast.makeText(homeView.getContext(),view.getTag().toString() , Toast.LENGTH_SHORT).show();

                                        GalleryFragment newGamefragment = new GalleryFragment();
                                        FragmentTransaction fragmentTransaction;
                                        Bundle arguments = new Bundle();
                                        arguments.putString("Seleccion",view.getTag().toString() );

                                        fragmentTransaction = getFragmentManager().beginTransaction();
                                        newGamefragment.setArguments(arguments);

                                        fragmentTransaction.replace(R.id.fragmento, newGamefragment);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                        ll.removeAllViews();
                                        x=0;
                                                  //intent
                                    }
                                });

                                layoutSeparar.get(x).removeAllViews();

                                layoutSeparar.get(x).addView(icono.get(x));
                                x++;
                            }

                            Log.d("mensaje", String.valueOf(x));

                            ll.removeAllViews();
                            for (int i=0;i<x;i++){
                                if(layoutSeparar.get(i).getParent() != null) {
                                    ((ViewGroup)layoutSeparar.get(i).getParent()).removeView(layoutSeparar.get(i)); // <- fix
                                }

                                ll.addView(layoutSeparar.get(i));
                            }






                        } else {
                            Log.d("mensaje", "Error getting documents: ", task.getException());
                        }
                    }
                });




        return homeView;
    }
}