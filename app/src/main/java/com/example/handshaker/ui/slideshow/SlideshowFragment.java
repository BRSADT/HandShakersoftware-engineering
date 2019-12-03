package com.example.handshaker.ui.slideshow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.braintreepayments.cardform.view.CardForm;
import com.example.handshaker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private ImageView imgTipoTarjeta;
    private TextView txtPropietario, txtCodigoSeguridad, txtNumeroTarjeta;
    private Spinner cmbMes, cmbAnio;
    private Button btnConfirmar;

    StorageReference storageRef;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseAuth mAuth;
    private static final int READ_REQUEST_CODE = 42;
    private FirebaseFirestore db;
    public boolean bndAux;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel = ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View vistaPagos = inflater.inflate(R.layout.fragment_slideshow, container, false);

        imgTipoTarjeta = (ImageView)vistaPagos.findViewById(R.id.imgTipoTarjeta);
        txtPropietario = (TextView) vistaPagos.findViewById(R.id.txtPropietario);
        txtCodigoSeguridad = (TextView) vistaPagos.findViewById(R.id.txtCodigoSeguridad);
        txtNumeroTarjeta = (TextView) vistaPagos.findViewById(R.id.txtNumeroTarjeta);
        cmbMes = (Spinner) vistaPagos.findViewById(R.id.cmbMes);
        cmbAnio = (Spinner) vistaPagos.findViewById(R.id.cmbAnio);
        btnConfirmar = (Button) vistaPagos.findViewById(R.id.btnConfirmar);

        bndAux = false;

        LogoTarjetaDefecto();
        LlenarMesAnio();

        txtNumeroTarjeta.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean TieneFocus) {
                if (!TieneFocus) {
                    TipoTarjeta();
                }
            }
        });

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Boton Confirmar", "si entró wey");
                if(EsValida()){
                    if(ConfirmarPago()){
                        RegistrarPago();
                    }
                }
            }
        });

        txtNumeroTarjeta.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                TipoTarjeta();
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        });

        return vistaPagos;
    }

    public void LogoTarjetaDefecto(){
        imgTipoTarjeta.setImageResource(R.drawable.logo_otro);
    }

    public void LlenarMesAnio(){
        ArrayList<String> spnAux = new ArrayList<String>();
        String itmAux;
        for(int i = 1; i < 13; i++){
            itmAux = String.valueOf(i);
            if(itmAux.length() < 2){
                itmAux = "0" + i;
            }
            spnAux.add(itmAux);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, spnAux);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        cmbMes.setAdapter(adapter);

        ArrayList<String> spnAux2 = new ArrayList<String>();
        for(int i = 19; i < 100; i++){
            itmAux = "20" + String.valueOf(i);
            spnAux2.add(itmAux);
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, spnAux2);
        adapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        cmbAnio.setAdapter(adapter2);
    }

    public boolean EsValida(){
        Log.i("EsValida","Ya entró a verificar la validación");
        if(txtPropietario.getText().toString().trim().equals("")){
            Toast.makeText(getActivity().getBaseContext(),"Nombre vacío" , Toast.LENGTH_SHORT).show();
            return false;
        } else if(txtCodigoSeguridad.getText().toString().trim().equals("") || txtCodigoSeguridad.getText().toString().trim().length() != 3) {
            Toast.makeText(getActivity().getBaseContext(), "Código incorrecto", Toast.LENGTH_SHORT).show();
            return false;
        } else if(txtNumeroTarjeta.getText().toString().trim().equals("") || txtNumeroTarjeta.getText().toString().trim().length() != 16){
            Toast.makeText(getActivity().getBaseContext(),"Número de Tarjeta incorrecto" , Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void TipoTarjeta(){
        String aux = txtNumeroTarjeta.getText().toString().trim();
        if(Pattern.matches("^4[0-9]{2,12}(?:[0-9]{3})?$",aux)){
            imgTipoTarjeta.setImageResource(R.drawable.logo_visa);
        }else if(Pattern.matches("^5[1-5][0-9]{1,14}$",aux)){
            imgTipoTarjeta.setImageResource(R.drawable.logo_mastercard);
        }else if(Pattern.matches("^3[47][0-9]{1,13}$",aux)){
            imgTipoTarjeta.setImageResource(R.drawable.logo_amex);
        }else{
            LogoTarjetaDefecto();
        }
    }

    public boolean ConfirmarPago(){
        bndAux = true;
        /*Log.i("Confirmar Pago", "Entró a confirmar pago");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getBaseContext());
        builder.setIcon(R.drawable.mb_ic_exit);
        builder.setCancelable(true);
        builder.setTitle("¿Desea continuar?");
        builder.setMessage("Está a punto de realizar una transacción, está acción no se puede deshacer más que directamente con su banco.\n¿Desea continuar con la operación?");
        builder.setPositiveButton("Realizar pago",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    bndAux = true;
                    Log.i("Confirmación", "Ya le diste que si, el valor de bndAux es " + bndAux);
                }
            });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bndAux = false;
                Log.i("Cancelación", "Ya le diste que no, el valor de bndAux es " + bndAux);
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();*/
        return bndAux;
    }

    public void RegistrarPago(){
        Log.i("Registro Pago","Empezó Registrar Pago");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put("PagoVigente", ObtenerFechaHoy());

        db.collection("Trabajador").document(mAuth.getUid()).update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity().getBaseContext(),"Se han actualizado los datos" , Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                });

        getActivity().finish();
    }

    public String ObtenerFechaHoy(){
        Date date = Calendar.getInstance().getTime();
        // Display a date in day, month, year format
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Log.i("Dia de hoy", formatter.format(date));
        return formatter.format(date);
    }
}