package com.example.handshaker;

import android.os.Bundle;

import com.example.handshaker.ui.NombresOficios.HomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.handshaker.GlideOptions.fitCenterTransform;

public class inicioTrabajador extends AppCompatActivity {
    private HomeViewModel homeViewModel;
    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;
    StorageReference storageRef;
    FirebaseStorage storage ;
    public TextView Nombre,Apellido, correo;
    StorageReference pathReference,fotoPerfil,referencia ;
    public ImageView avatar;
    private  FirebaseFirestore db;
    ArrayList<LinearLayout> layoutSeparar=new ArrayList<LinearLayout>();
    ArrayList<ImageButton> icono=new ArrayList<ImageButton >();
    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        mAuth = FirebaseAuth.getInstance();


       drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();

        db = FirebaseFirestore.getInstance();

        db.collection("Trabajador")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("datos-tra", document.getId() + " => " + document.getData());

                                if (document.getId().equals(mAuth.getUid())) {
                                    Log.d("datos", "encontrado es trabajador");
                                    mAppBarConfiguration = new AppBarConfiguration.Builder(
                                            R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                                            R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                                            .setDrawerLayout(drawer)
                                            .build();

                                }
                            }
                        } else {

                        }
                    }
                });



        db.collection("Clientes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("datos-cliente", document.getId() + " => " + document.getData());

                                if (document.getId().equals(mAuth.getUid())) {
                                    //  Toast.makeText(getApplicationContext(), "es cliente", Toast.LENGTH_SHORT).show();
                                    Log.d("datos", "encontrado es cliente");
                                    mAppBarConfiguration = new AppBarConfiguration.Builder(
                                            R.id.nav_home, R.id.nav_gallery,
                                            R.id.nav_tools, R.id.nav_share)
                                            .setDrawerLayout(drawer)
                                            .build();
                                }
                            }
                        } else {

                        }
                    }
                });



        NavController navController = Navigation.findNavController(this, R.id.fragmento);
       NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
       NavigationUI.setupWithNavController(navigationView, navController);


        FirebaseStorage storage = FirebaseStorage.getInstance();
        View hView = navigationView.getHeaderView(0);







//CAMBIAR LA BARRA LATERAL
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        storageRef = storage.getReference();
        db = FirebaseFirestore.getInstance();
        Nombre=(TextView) hView.findViewById(R.id.Nombreuser);
        correo=(TextView) hView.findViewById(R.id.correoUser);
        avatar = (ImageView)hView.findViewById(R.id.avatarUsuario);

        referencia = storage.getReference().child(mAuth.getUid()).child("profile.jpg");


       GlideApp.with(this )
                .load(referencia)
                .into(avatar);
        DocumentReference docRef = db.collection("Trabajador").document(mAuth.getUid());
        Source source = Source.CACHE;
        docRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
                    document.get("Apellido");
                    document.get("Nombre");
                    Nombre.setText(document.get("Nombre").toString()+" "+document.get("Apellido").toString());
                    correo.setText(mAuth.getCurrentUser().getEmail());
                    Log.d("Datos-info del usuario", "Cached document data: " + document.getData());
                } else {
                    Log.d("Datos-Error", "Cached get failed: ", task.getException());
                }
            }
        });




   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.prueba, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.fragmento);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}
