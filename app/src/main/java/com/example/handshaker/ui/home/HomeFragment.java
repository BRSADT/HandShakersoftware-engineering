package com.example.handshaker.ui.home;

import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.handshaker.R;
import com.example.handshaker.ui.gallery.GalleryFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    ArrayList<LinearLayout> layoutSeparar=new ArrayList<LinearLayout>();
    ArrayList<Button> icono=new ArrayList<Button >();
    private  FirebaseFirestore db;
    private HomeViewModel homeViewModel;
    View homeView;
    int x=0;
    LinearLayout ll;
    LinearLayout.LayoutParams o;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
         homeView = inflater.inflate(R.layout.fragment_home, container, false);
       // final TextView textView = root.findViewById(R.id.text_home);
     /*   homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
    */


         ll = (LinearLayout)homeView.findViewById(R.id.Scroll);
         o = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        db = FirebaseFirestore.getInstance();
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
                                icono.add(new Button(homeView.getContext()));
                                icono.get(x).setLayoutParams( new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 2.0f));
                                icono.get(x).setText(document.get("nombreOficio").toString());
                                icono.get(x).setTag(document.get("nombreOficio").toString());
                                icono.get(x).setBackgroundResource(R.drawable.driver);
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

                                                  //intent
                                    }
                                });

                                layoutSeparar.get(x).addView(icono.get(x));
                                x++;
                            }

                            Log.d("mensaje", String.valueOf(x));


                            for (int i=0;i<x;i++){
                                Log.d("mensaje", String.valueOf(i) );

                                ll.addView(layoutSeparar.get(i));
                            }






                        } else {
                            Log.d("mensaje", "Error getting documents: ", task.getException());
                        }
                    }
                });


  /*      for (int x=0;x<10;x++){

            layoutSeparar.add(new LinearLayout(homeView.getContext()));
            o.setMargins(0,0,0,10);
            layoutSeparar.get(x).setLayoutParams(o);
            layoutSeparar.get(x).setOrientation(LinearLayout.HORIZONTAL);
            icono.add(new Button(homeView.getContext()));
            icono.get(x).setLayoutParams( new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 2.0f));
            icono.get(x).setText("x");
            icono.get(x).setBackgroundResource(R.drawable.driver);
            layoutSeparar.get(x).addView(icono.get(x));

        }

    */


        return homeView;
    }
}