package com.example.handshaker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.core.FirestoreClient;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class DatosTrabajadores extends AppCompatActivity {
    StorageReference storageRef;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    public EditText txtNombre,txtApellido;
    Button guardar;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_trabajadores);
        guardar= (Button)findViewById(R.id.btnEnviar);
        txtNombre= (EditText) findViewById(R.id.txtNombre);
        txtApellido= (EditText) findViewById(R.id.txtApellido);
        avatar=(ImageButton)  findViewById(R.id.avatar);
        //avatar=(Button)  findViewById(R.id.avatar);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        storageRef = storage.getReference();

        final StorageReference Nusuario = storageRef.child(mAuth.getUid());
        final StorageReference tipo = Nusuario.child("profile.jpg");


        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"guardar..." , Toast.LENGTH_SHORT).show();

                Map<String, Object> user = new HashMap<>();
                user.put("Nombre", txtNombre.getText().toString());
                user.put("Apellido", txtApellido.getText().toString());

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




}
