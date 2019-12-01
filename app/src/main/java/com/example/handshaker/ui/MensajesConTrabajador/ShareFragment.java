package com.example.handshaker.ui.MensajesConTrabajador;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.handshaker.R;
import com.example.handshaker.ui.Mensaje.SendFragment;

public class ShareFragment extends Fragment {

    private ShareViewModel shareViewModel;
    Button btnEnviarMensaje;
    String sel="";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        shareViewModel =
                ViewModelProviders.of(this).get(ShareViewModel.class);
        View MsnTrabajador = inflater.inflate(R.layout.fragment_share, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            sel = bundle.getString("IdTrabajador");
            Toast.makeText(this.getContext(),sel , Toast.LENGTH_SHORT).show();

        }


        btnEnviarMensaje= MsnTrabajador.findViewById(R.id.btnEnviarMSN);

        //se va al fragmento de la clase SendFragment que es para mandar mensajes.
        btnEnviarMensaje.setOnClickListener(new View.OnClickListener() {
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






        return MsnTrabajador;
    }
}