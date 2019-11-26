package com.example.handshaker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
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

public class DatosClientes extends AppCompatActivity {

    StorageReference storageRef;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public EditText txtNombre,txtApellido, txtCorreoContacto, txtTelefono;
    Button btnGuardar;
    ImageButton btnAvatar;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private static final int SELECT_FILE = 1;
    private static final int REQUEST_CODE = 1;
    private static final int READ_REQUEST_CODE = 42;
    private FirebaseFirestore db;
    ImageView imageView;
    private Bitmap bitmap;
    Drawable drawable;
    String rutaPerfil;
    StorageReference mountainsRef;

    StorageReference mountainImagesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_clientes);
        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtApellido = (EditText) findViewById(R.id.txtApellido);
        txtCorreoContacto = (EditText) findViewById(R.id.txtCorreoContacto);
        txtTelefono = (EditText) findViewById(R.id.txtTelefono);
        btnGuardar= (Button)findViewById(R.id.btnEnviar);
        btnAvatar=(ImageButton)  findViewById(R.id.avatar);

        bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_avatardefecto);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        storageRef = storage.getReference();

        final StorageReference Nusuario = storageRef.child(mAuth.getUid());
        final StorageReference tipo = Nusuario.child("profile.jpg");

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ValidarCaptura()){
                    Toast.makeText(getApplicationContext(),"Guardando los cambios..." , Toast.LENGTH_SHORT).show();

                    Map<String, Object> user = new HashMap<>();
                    user.put("Nombre", txtNombre.getText().toString());
                    user.put("Apellido", txtApellido.getText().toString());
                    user.put("Telefono", txtTelefono.getText().toString());
                    user.put("CorreoContacto", txtCorreoContacto.getText().toString());
                    user.put("FechaReg", ObtenerFechaHoy());

                    //Add a new document with a generated ID
                    db.collection("Clientes").document(mAuth.getUid()).set(user)
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

        btnAvatar.setOnClickListener(new View.OnClickListener() {
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

                btnAvatar.setImageBitmap(bitmap.createScaledBitmap(bitmap,btnAvatar.getWidth(),btnAvatar.getHeight(),true)); //


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean ValidarCaptura(){
        if(txtNombre.getText().toString().equals("") || txtApellido.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"ERROR: Nombre o apellido vacío" , Toast.LENGTH_LONG).show();
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
