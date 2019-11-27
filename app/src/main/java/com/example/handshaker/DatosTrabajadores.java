package com.example.handshaker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.FirestoreClient;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class DatosTrabajadores extends AppCompatActivity {
    StorageReference storageRef;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public EditText txtNombre,txtApellido, txtCorreoContacto, txtTelefono, txtInfo;
    public Spinner cmbOficio, cmbHor1, cmbHor2;
    Button guardar;
    CheckBox chkbxUrgencia;
    ImageButton avatar;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private static final int SELECT_FILE = 1;
    private static final int REQUEST_CODE = 1;
    private static final int READ_REQUEST_CODE = 42;
    private  FirebaseFirestore db;
    ImageView imageView;
    private Bitmap bitmap;
    Drawable drawable;
    String rutaPerfil;
    StorageReference mountainsRef;

    StorageReference mountainImagesRef;

    public ArrayList<String> horarios = new ArrayList<String>();
    public ArrayList<String> oficios = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_trabajadores);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtApellido = (EditText) findViewById(R.id.txtApellido);
        txtCorreoContacto = (EditText) findViewById(R.id.txtCorreoContacto);
        txtTelefono = (EditText) findViewById(R.id.txtTelefono);
        txtInfo = (EditText) findViewById(R.id.txtInfo);

        LlenarHorarios();
        ObtenerOficios();
        oficios.add("Otro");

        cmbOficio = (Spinner) findViewById(R.id.cmbOficio);
        cmbHor1 = (Spinner) findViewById(R.id.cmbHor1);
        cmbHor2 = (Spinner) findViewById(R.id.cmbHor2);

        if(horarios == null){
            Toast.makeText(getApplicationContext(),"No se puede crear los horarios" , Toast.LENGTH_SHORT).show();
            horarios.add("ERROR");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, horarios);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        cmbHor1.setAdapter(adapter);
        cmbHor2.setAdapter(adapter);
        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, oficios);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        cmbOficio.setAdapter(adapter);

        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_avatardefecto);

        chkbxUrgencia = (CheckBox) findViewById(R.id.chkbxUrgencia);
        guardar= (Button)findViewById(R.id.btnEnviar);
        avatar=(ImageButton)  findViewById(R.id.avatar);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        storageRef = storage.getReference();

        final StorageReference Nusuario = storageRef.child(mAuth.getUid());
        final StorageReference tipo = Nusuario.child("profile.jpg");


        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ValidarCaptura()){
                    Toast.makeText(getApplicationContext(),"Guardando los cambios..." , Toast.LENGTH_SHORT).show();

                    Map<String, Object> user = new HashMap<>();
                    user.put("Nombre", txtNombre.getText().toString());
                    user.put("Apellido", txtApellido.getText().toString());
                    user.put("Oficio", cmbOficio.getSelectedItem().toString());
                    user.put("Telefono", txtTelefono.getText().toString());
                    //user.put("Curriculum", ObtenerCurriculum());
                    user.put("Horario", cmbHor1.getSelectedItem().toString() + "-" + cmbHor2.getSelectedItem().toString());
                    user.put("CorreoContacto", txtCorreoContacto.getText().toString());
                    user.put("FechaReg", ObtenerFechaHoy());
                    user.put("Urgencia", chkbxUrgencia.isChecked());
                    user.put("PagoVigente", false);
                    String resumen = txtInfo.getText().toString();
                    user.put("Info", resumen); //AQUI

                    //Add a new document with a generated ID
                    db.collection("Trabajador").document(mAuth.getUid()).set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"Se ha guardado datos" , Toast.LENGTH_SHORT).show();


                                }
                            });

                    //subir archivos

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    UploadTask uploadTask = tipo.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            // ...
                        }
                    });
                }
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                abrirGaleria(view);
            }
        });

    }
    @Override

    public void onStart() {

        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.

       FirebaseUser currentUser = mAuth.getCurrentUser();
         //    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
    }


    public void abrirGaleria(View v){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("image/*");

        startActivityForResult(intent, READ_REQUEST_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == READ_REQUEST_CODE  && resultCode == Activity.RESULT_OK)
            try {

                if (bitmap != null) {
                    bitmap.recycle();
                }
                InputStream stream = getContentResolver().openInputStream(
                        data.getData());

                bitmap = BitmapFactory.decodeStream(stream);
                stream.close();

                avatar.setImageBitmap(bitmap.createScaledBitmap(bitmap,avatar.getWidth(),avatar.getHeight(),true)); //


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void LlenarHorarios(){
        int aux = 0;
        String aux2 = "0";
        for(int i = 0; i < 48; i++){
            if(aux == 30){
                aux2 = "30";
                aux = 0;
            }
            else{
                aux2 = "00";
                aux = 30;
            }
            if(String.valueOf((int) i / 2).length() < 2){
                aux2 = "0" + String.valueOf((int) i / 2) + ":" + aux2;
            }
            else{
                aux2 = String.valueOf((int) i / 2) + ":" + aux2;
            }
            Log.i("Mensaje de aux2", aux2);
            horarios.add(aux2);
        }
    }

    public void ObtenerOficios(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Oficios").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        oficios.add(document.get("nombreOficio").toString());
                        Log.i("Elemento Agregado", "Se agrego el oficio: " + document.get("nombreOficio").toString() + "al ArrayList del spinner cmbOficios");
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"ERROR: No se pudieron obtener los oficios" , Toast.LENGTH_LONG).show();
                    Log.e("Obtener Oficios", "No se pudieron obtener los oficios: ", task.getException());
                }
            }
        });
    }

    public boolean ValidarCaptura(){
        if(txtNombre.getText().toString().trim().equals("") || txtApellido.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(),"ERROR: Nombre o apellido vacío" , Toast.LENGTH_LONG).show();
            return false;
        }
        if(cmbHor1.getSelectedItem().toString().equals(cmbHor2.getSelectedItem().toString())){
            Toast.makeText(getApplicationContext(),"ERROR: Horario de entrada y salida iguales" , Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public String ObtenerFechaHoy(){
        Date date = Calendar.getInstance().getTime();
        // Display a date in day, month, year format
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Log.i("Dia de hoy", formatter.format(date));
        return formatter.format(date);
    }

}
