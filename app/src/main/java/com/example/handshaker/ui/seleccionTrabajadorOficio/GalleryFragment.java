package com.example.handshaker.ui.seleccionTrabajadorOficio;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.handshaker.ui.NombresOficios.HomeViewModel;
import com.example.handshaker.ui.TrabajadorSeleccionado.ToolsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {
String sel="";
    private GalleryViewModel galleryViewModel;
    LinearLayout ll;
    FirebaseStorage storage ;
    StorageReference pathReference,fotoPerfil,referencia ;
    LinearLayout.LayoutParams o,Parfotos,ParDatos;
    ArrayList<LinearLayout> layoutSeparar=new ArrayList<LinearLayout>();
    ArrayList<LinearLayout> layoutFotos=new ArrayList<LinearLayout>();
    ArrayList<LinearLayout> layoutDatos=new ArrayList<LinearLayout>();
    ArrayList<ImageView> icono=new ArrayList<ImageView>();

    ArrayList<TextView> nombrecompleto=new ArrayList<TextView>();

    private FirebaseFirestore db;
    private HomeViewModel OficioViewModel;
    View OficioView;
    int x=0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        OficioView = inflater.inflate(R.layout.fragment_gallery, container, false);
        ll = (LinearLayout)OficioView.findViewById(R.id.Scroll);
        o = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        Parfotos = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 2);
        ParDatos = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);





        Bundle bundle = this.getArguments();
        if (bundle != null) {
            sel = bundle.getString("Seleccion");
            Toast.makeText(this.getContext(),sel , Toast.LENGTH_SHORT).show();

        }

        //CONSULTA Personal
        db = FirebaseFirestore.getInstance();
        db.collection("Trabajador").whereEqualTo("Oficio", sel)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("mensaje", document.getId() + " => " + document.getData());

                                layoutSeparar.add(new LinearLayout( OficioView.getContext()));
                                o.setMargins(0,0,0,10);
                                layoutSeparar.get(x).setLayoutParams(o);
                                layoutSeparar.get(x).setOrientation(LinearLayout.HORIZONTAL);
                                layoutSeparar.get(x).setTag(document.getId().toString());
                                layoutSeparar.get(x).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                       Toast.makeText(OficioView.getContext(),"dio click "+view.getTag().toString() , Toast.LENGTH_SHORT).show();
                                        //abrir datos del seleccionado
                                        x=0;
                                        ToolsFragment newGamefragment = new ToolsFragment();
                                        FragmentTransaction fragmentTransaction;
                                        Bundle arguments = new Bundle();
                                        arguments.putString("IdTrabajador",view.getTag().toString() );

                                        fragmentTransaction = getFragmentManager().beginTransaction();
                                        newGamefragment.setArguments(arguments);

                                        fragmentTransaction.replace(R.id.fragmento, newGamefragment);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();



                                    }
                                });

                                //Dentro de este layout
                                layoutFotos.add(new LinearLayout( OficioView.getContext()));
                                Parfotos.setMargins(0,0,0,10);
                                layoutFotos.get(x).setLayoutParams(Parfotos);

                                layoutFotos.get(x).setOrientation(LinearLayout.HORIZONTAL);



                                layoutDatos.add(new LinearLayout( OficioView.getContext()));
                                  ParDatos.setMargins(0,0,0,10);
                                layoutDatos.get(x).setBackgroundColor(R.color.md_blue_grey_500);
                                layoutDatos.get(x).setLayoutParams(ParDatos);
                                layoutDatos.get(x).setOrientation(LinearLayout.VERTICAL);
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                               referencia = storage.getReference().child(document.getId()).child("profile.jpg");





                                icono.add(new ImageView(OficioView.getContext()));
                                icono.get(x).setLayoutParams( new LinearLayout.LayoutParams(0,ViewGroup.LayoutParams.WRAP_CONTENT,1.0f));

                                GlideApp.with(OficioView )
                                        .load(referencia)
                                        .into(icono.get(x));


                               nombrecompleto.add(new TextView(OficioView.getContext()));
                                nombrecompleto.get(x).setLayoutParams(o);
                                nombrecompleto.get(x).setTextSize(15);


                                  nombrecompleto.get(x).setText(document.get("Nombre").toString()+" "+ document.get("Apellido").toString());

                                if(nombrecompleto.get(x).getParent() != null) {
                                    ((ViewGroup)nombrecompleto.get(x).getParent()).removeView(nombrecompleto.get(x)); // <- fix
                                }

                                if(icono.get(x).getParent() != null) {
                                    ((ViewGroup)icono.get(x).getParent()).removeView(icono.get(x)); // <- fix
                                }
                                if(layoutFotos.get(x).getParent() != null) {
                                    ((ViewGroup)layoutFotos.get(x).getParent()).removeView(layoutFotos.get(x)); // <- fix
                                }
                                if(layoutDatos.get(x).getParent() != null) {
                                    ((ViewGroup)layoutDatos.get(x).getParent()).removeView(layoutDatos.get(x)); // <- fix
                                }

                                layoutDatos.get(x).addView(nombrecompleto.get(x));

                              layoutFotos.get(x).addView(icono.get(x));

                                layoutSeparar.get(x).addView(layoutFotos.get(x));
                                layoutSeparar.get(x).addView(layoutDatos.get(x));
                                x++;
                            }

                            Log.d("mensaje", String.valueOf(x));


                            for (int i=0;i<x;i++){
                                Log.d("mensaje", String.valueOf(i) );
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




        return OficioView;
    }
}