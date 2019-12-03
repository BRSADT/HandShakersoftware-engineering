package com.example.handshaker.ui.TrabajadorSeleccionado;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.handshaker.CalificacionDatos;
import com.example.handshaker.GlideApp;
import com.example.handshaker.R;
import com.example.handshaker.inicioSesion;
import com.example.handshaker.inicioTrabajador;
import com.example.handshaker.ui.Mensaje.SendFragment;
import com.example.handshaker.ui.seleccionTrabajadorOficio.GalleryFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ToolsFragment extends Fragment  implements Serializable  {

    private ToolsViewModel toolsViewModel;
    String sel="";
    private FirebaseAuth mAuth;
    ImageView fotoTrabajador;
    TextView nombre,apellido,oficio,celular,txtInfoT,Horario1,tel,txtCorreo;
    View TrabajadorView;
    StorageReference storageRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference referenciaChat = database.getReference();
    FirebaseStorage storage ;
    StorageReference referencia,referenciaT ,referencia2;
    ScrollView ScrollMensajes;
    RatingBar rb;
    private  FirebaseFirestore db;
    LinearLayout ll;
    ArrayList<LinearLayout> layoutFotos=new ArrayList<LinearLayout>();
    ArrayList<LinearLayout> layoutDatos=new ArrayList<LinearLayout>();
    ArrayList<LinearLayout> layoutSeparar1=new ArrayList<LinearLayout>();
    ArrayList<LinearLayout> layoutSeparar2=new ArrayList<LinearLayout>();
    ArrayList<TextView> comentarios=new ArrayList<TextView>();
    ArrayList<LinearLayout> principal=new ArrayList<LinearLayout>();
    Button btnSolicitar;
    LinearLayout.LayoutParams o,Parfotos,Pseparar1,Pseparar2,PDatos,Pprincipal;
    int  x=0;
    ArrayList<LinearLayout> lcomentario=new ArrayList<LinearLayout>();
    ArrayList<ImageView> foto=new ArrayList<ImageView>();
    ArrayList<String> IDusuarios=new ArrayList<String>();
    ArrayList<TextView> comentario=new ArrayList<TextView>();
    ArrayList<ImageView> icono=new ArrayList<ImageView>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        final View TrabajadorView = inflater.inflate(R.layout.fragment_tools, container, false);
        fotoTrabajador=(ImageView)TrabajadorView.findViewById(R.id.fotoPerfil);
        nombre=(TextView) TrabajadorView.findViewById(R.id.Nombre);
        oficio=(TextView) TrabajadorView.findViewById(R.id.Oficio);
        txtInfoT=(TextView) TrabajadorView.findViewById(R.id.txtInfoT);
        btnSolicitar=(Button) TrabajadorView.findViewById(R.id.btnSolicitar);
        Horario1=(TextView) TrabajadorView.findViewById(R.id.Horario1);
        tel=(TextView) TrabajadorView.findViewById(R.id.tel);
        txtCorreo=(TextView) TrabajadorView.findViewById(R.id.txtCorreo);
        rb = (RatingBar) TrabajadorView.findViewById(R.id.ratingBar);
        ScrollMensajes=TrabajadorView.findViewById(R.id.ComentariosUsuarios);
        o = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        Parfotos = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 2);
        Pseparar1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 2);
        Pseparar2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 2);

        Pprincipal = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 2);
        PDatos = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 2);



        ll = (LinearLayout)TrabajadorView.findViewById(R.id.scrollCom);




        FloatingActionButton fab =  TrabajadorView.findViewById(R.id.fab);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            sel = bundle.getString("IdTrabajador");
            Toast.makeText(this.getContext(),sel , Toast.LENGTH_SHORT).show();

        }
        //dar click a Mensaje
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               SendFragment newGamefragment = new SendFragment();
                FragmentTransaction fragmentTransaction;
                Bundle arguments = new Bundle();
                arguments.putString("IdTrabajador",sel);

                fragmentTransaction = getFragmentManager().beginTransaction();
                newGamefragment.setArguments(arguments);

                fragmentTransaction.replace(R.id.fragmento, newGamefragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();



            }
        });


//cambiar datos






        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
         storage = FirebaseStorage.getInstance();
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
                    tel.setText(document.get("Telefono").toString());
                    txtInfoT.setText(document.get("Info").toString());
                    Horario1.setText(document.get("Horario").toString());
                    txtCorreo.setText(document.get("CorreoContacto").toString());



                    Log.d("Datos-info del usuario", "Cached document data: " + document.getData());
                } else {
                    Log.d("Datos-Error", "Cached get failed: ", task.getException());
                }
            }
        });


        rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Intent i = new Intent(getActivity(), CalificacionDatos.class);
                i.putExtra("IdTrabajador",(Serializable)sel);
                getActivity().startActivity(i);

            }
        });


        //Si se ha escrito comentario
        referenciaChat.child("Calificaciones").child(sel).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {



                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Log.i("mensaje2", child.getKey().toString());
                    Log.i("mensaje2", child.getValue().toString());
                    Log.i("mensaje2", child.child("usuario").getValue().toString());





                            IDusuarios.add(child.child("usuario").getValue().toString());
                            Log.i("mensaje2", IDusuarios.get(x));
                           lcomentario.add(new LinearLayout(TrabajadorView.getContext()));

                            o.setMargins(0, 0, 0, 10);
                    Parfotos .setMargins(0, 0, 0, 10);
                    Pseparar1.setMargins(0, 0, 0, 10);
                    Pprincipal .setMargins(0, 0, 0, 10);

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

                    }

                for (int i=0;i<x;i++){

                    ll.addView(principal.get(i));
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

//BOTON
        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> datos = new HashMap<>();
                Log.i("MENSAJE3",btnSolicitar.getText().toString());

                switch (btnSolicitar.getText().toString()) {
                    case "Solicitar":
                        Log.i("MENSAJE3","askdj");
                        btnSolicitar.setText("ESPERANDO CONFIRMACION");
                        datos.put("estado", "ESPERANDO CONFIRMACION");
                        db.collection("Trabajador").document(sel).collection("Clientes").document(mAuth.getUid()).set(datos)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                          @Override
                                                          public void onSuccess(Void aVoid) {


                                                          }});
                        break;
                    case "ESPERANDO CONFIRMACION":


                        break;
                    case "FINALIZAR":
                        datos.put("estado", "Solicitar");
                        db.collection("Trabajador").document(sel).collection("Clientes").document(mAuth.getUid()).set(datos)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {


                                    }});
                        break;
                }


                //Add a new document with a generated ID
            }
        });


//verificar estado boton
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference documentReference = db.collection("Trabajador").document(sel).collection("Clientes").document(mAuth.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot != null && documentSnapshot.exists()) {
                        Log.i("MENSAJE3","EXISTE");
                        Log.d("MENSAJE3", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                        Log.d("MENSAJE3", documentSnapshot.get("estado").toString() + " => " + documentSnapshot.getData());
                        btnSolicitar.setText(documentSnapshot.get("estado").toString());




                    }else{
                        Log.i("MENSAJE3","NO EXISTE");  }

                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("MENSAJE3","ERROR");
                    }
                });


        return TrabajadorView;
}}