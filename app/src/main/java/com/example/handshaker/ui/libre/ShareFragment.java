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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShareFragment extends Fragment {
    StorageReference storageRef;
    private FirebaseAuth mAuth;
    private ShareViewModel shareViewModel;
    Button btnEnviarMensaje;
    StorageReference storageReference;
    LinearLayout ll;
    ArrayList<LinearLayout> layoutFotos=new ArrayList<LinearLayout>();
    ArrayList<LinearLayout> layoutDatos=new ArrayList<LinearLayout>();
    ArrayList<LinearLayout> layoutSeparar1=new ArrayList<LinearLayout>();
    ArrayList<LinearLayout> layoutSeparar2=new ArrayList<LinearLayout>();
    ArrayList<LinearLayout> lcomentario=new ArrayList<LinearLayout>();
    StorageReference referencia,referenciaT ,referencia2;
    ArrayList<ImageView> foto=new ArrayList<ImageView>();
    ArrayList<String> IDusuarios=new ArrayList<String>();
    ArrayList<TextView> comentario=new ArrayList<TextView>();
    ArrayList<ImageView> icono=new ArrayList<ImageView>();
    ArrayList<Button> btnEstado=new ArrayList<Button>();
    ArrayList<Button> btnChat=new ArrayList<Button>();

    ArrayList<TextView> comentarios=new ArrayList<TextView>();
    ArrayList<LinearLayout> principal=new ArrayList<LinearLayout>();
    LinearLayout.LayoutParams o,Parfotos,Pseparar1,Pseparar2,PDatos,Pprincipal,Pbotones;
    int  x=0;
    View VistaSolicitud;
    FirebaseStorage storage ;
    String sel="";
    private  FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareViewModel =
                ViewModelProviders.of(this).get(ShareViewModel.class);
        VistaSolicitud= inflater.inflate(R.layout.fragment_share, container, false);


        o = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        Parfotos = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3);
        Pseparar1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        Pseparar2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        PDatos = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 4);
        Pbotones= new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 3);
        Pprincipal = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,1);



        ll = (LinearLayout)VistaSolicitud.findViewById(R.id.scrollSol);




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
                                       IDusuarios.add(document.getId());




                                Parfotos.setMargins(0, 0, 0, 10);
                                Pseparar1.setMargins(0, 0, 0, 10);
                                Pprincipal.setMargins(0, 0, 0, 10);

                                principal.add(new LinearLayout(VistaSolicitud.getContext()));
                                principal.get(x).setLayoutParams(Pprincipal);
                                principal.get(x).setOrientation(LinearLayout.HORIZONTAL);

                                layoutSeparar1.add(new LinearLayout(VistaSolicitud.getContext()));
                                layoutSeparar1.get(x).setLayoutParams(Pseparar1);


                                layoutDatos.add(new LinearLayout(VistaSolicitud.getContext()));
                                layoutDatos.get(x).setLayoutParams(PDatos);

                                       lcomentario.add(new LinearLayout(VistaSolicitud.getContext()));

                                comentarios.add(new TextView(VistaSolicitud.getContext()));
                                comentarios.get(x).setText(document.get("estado").toString());


                                layoutSeparar2.add(new LinearLayout(VistaSolicitud.getContext()));
                                layoutSeparar2.get(x).setLayoutParams(Pseparar2);

                                layoutFotos.add(new LinearLayout(VistaSolicitud.getContext()));
                                layoutFotos.get(x).setLayoutParams(Parfotos);

                                layoutFotos.get(x).setOrientation(LinearLayout.HORIZONTAL);


                                btnEstado.add(new Button(VistaSolicitud.getContext()));
                                       btnEstado.get(x).setLayoutParams( Pbotones);
                                       if (document.get("estado").toString().equals("ESPERANDO CONFIRMACION")){
                                           btnEstado.get(x).setText("Aceptar");
                                       }else{

                                           btnEstado.get(x).setText("Finalizado");
                                       }

                                       btnEstado.get(x).setTag(x);
                                       btnEstado.get(x).setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               Map<String, Object> datos = new HashMap<>();
                                                    String mensaje=btnEstado.get(Integer.parseInt(String.valueOf(view.getTag()))).getText().toString();
                                                    String idUsuarioSolicitud=IDusuarios.get(Integer.parseInt(String.valueOf(view.getTag())));
                                               switch (mensaje) {
                                                   case "Aceptar":
                                                       Log.i("MENSAJE3","askdj");
                                                       btnEstado.get(Integer.parseInt(String.valueOf(view.getTag()))).setText("ESPERANDO FINALICE");
                                                       datos.put("estado", "FINALIZAR");
                                                       db.collection("Trabajador").document(mAuth.getUid()).collection("Clientes").document(idUsuarioSolicitud).set(datos)
                                                               .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                   @Override
                                                                   public void onSuccess(Void aVoid) {


                                                                   }});
                                                       break;

                                               }



                                           }
                                       });


                                       btnChat.add(new Button(VistaSolicitud.getContext()));
                                       btnChat.get(x).setTag(IDusuarios.get(x));
                                       btnChat.get(x).setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
//                                        Toast.makeText(homeView.getContext(),view.getTag().toString() , Toast.LENGTH_SHORT).show();
                                               SendFragment newGamefragment = new SendFragment();
                                               FragmentTransaction fragmentTransaction;
                                               Bundle arguments = new Bundle();
                                               arguments.putString("IdDestinatario",view.getTag().toString());

                                               fragmentTransaction = getFragmentManager().beginTransaction();
                                               newGamefragment.setArguments(arguments);

                                               fragmentTransaction.replace(R.id.fragmento, newGamefragment);
                                               fragmentTransaction.addToBackStack(null);
                                               fragmentTransaction.commit();

                                               //intent
                                           }
                                       });

                                icono.add(new ImageView(VistaSolicitud.getContext()));
                                icono.get(x).setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                referencia2 = storage.getReference().child(IDusuarios.get(x)).child("profile.jpg");

                                GlideApp.with(VistaSolicitud)
                                        .load(referencia2)
                                        .into(icono.get(x));


                                layoutDatos.get(x).addView(btnEstado.get(x));

                                       layoutDatos.get(x).addView(layoutSeparar1.get(x));
                                       layoutDatos.get(x).addView(btnChat.get(x));
                                       layoutFotos.get(x).addView(icono.get(x));


                                principal.get(x).addView(layoutFotos.get(x));
                                principal.get(x).addView(layoutSeparar2.get(x));
                                principal.get(x).addView(layoutDatos.get(x));


                                x++;


                                }
                            else{


                                }
                            }


                          for (int i=0;i<x;i++){
                                Log.d("mensaje", String.valueOf(i) );
                              if(principal.get(i).getParent() != null) {
                                  ((ViewGroup)principal.get(i).getParent()).removeView(principal.get(i)); // <- fix
                              }

                              ll.addView( principal.get(i));
                            }






                        } else {
                            Log.d("mensaje", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return VistaSolicitud;
    }
}