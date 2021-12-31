package com.example.dbreal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    private final String[] permisos = { Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ArrayList<Regalos> regalos;
    private final int ACTIVITY_CAMARA = 50;
    private final int ACTIVITY_GALERIA = 60;
    private ImageView foto;
    private Bitmap bitmap;

    private FirebaseAuth mAuth;
    private FirebaseUser usuario;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private TextView titulo,idMensaje,uri;
    private int contador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        regalos = new ArrayList<Regalos>();
        foto=findViewById(R.id.foto);
        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // abrir otra main Toast.makeText(MainActivity.this, "Tocaste la foto", Toast.LENGTH_SHORT).show();
            }
        });
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            requestPermissions(permisos, 100);
        }

        bitmap = null;

        mAuth=FirebaseAuth.getInstance();
        usuario=mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference =database.getReference();
        contador=0;
        titulo=findViewById(R.id.crearTitulo);
        //mensaje=findViewById(R.id.crearMensaje);
        //idMensaje=findViewById(R.id.buscarMensaje);
        uri=findViewById(R.id.rutafoto);

    }
    public void CrearMensaje(View view){
        contador ++;
        String ft= uri.getText().toString();
        String calveUnica = reference.push().getKey();
        reference.child("lista").child(usuario.getUid()).child("regalos"+calveUnica).child("foto").setValue(ft);
        reference.child("lista").child(usuario.getUid()).child("regalos"+calveUnica).child("titulo").setValue(titulo.getText().toString());
        //reference.child("mensajes").child(usuario.getUid()).child("mensaje"+contador).child("mensaje").setValue(mensaje.getText().toString());

        //reference.child("mensajes").child(usuario.getUid()).child("mensaje "+contador).child("idSecreto").setValue(calveUnica);
        Toast.makeText(this,"Regalo Añadido a la Lista "+contador,Toast.LENGTH_LONG).show();
        titulo.setText("");
        uri.setText("");



    }

    public void BuscarMensaje(View view){
        DatabaseReference referenciaMeensajes = FirebaseDatabase.getInstance()
                .getReference("lista").child(usuario.getUid());
                //.child("regalos "+.getText().toString());

        referenciaMeensajes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()) {
                    Log.i("MyTag", child.child("titulo").getValue(String.class));
                    regalos.add(new Regalos(snapshot.child("titulo").getValue(String.class),snapshot.child("foto").getValue(String.class)));
                    //imagesDir.add(String.valueOf(child.getValue()));
                }
                //String tituloR = snapshot.child("titulo").getValue(String.class);
                //regalos.add(new Regalos(snapshot.child("titulo").getValue(String.class),snapshot.child("foto").getValue(String.class)));
                //Toast.makeText(MainActivity2.this, "regalo"+tituloR, Toast.LENGTH_LONG).show();
                Intent intent = new Intent( MainActivity2.this,MainActivity3.class);
                intent.putExtra("regalos",regalos);
                startActivity(intent);
                //AlertDialog.Builder dialogo =new AlertDialog.Builder(MainActivity2.this);

                //dialogo.setTitle(snapshot.child("titulo").getValue(String.class))
                        //.setMessage(snapshot.child("foto").getValue(String.class))
                        //.setCancelable(true).create().show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity2.this, "No se encontro mensaje ", Toast.LENGTH_LONG).show();

            }
        });

    }

    public void CerrarSesion(View view){
        mAuth.signOut();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100){
            /*if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permiso de cámara concedido", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Se necesita acceso a la cámara. Por favor conceda el permiso", Toast.LENGTH_LONG).show();
            }*/

            if(!(grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                Toast.makeText(this, "Se necesita permiso de cámara", Toast.LENGTH_LONG).show();
            }

            if(!(grantResults[1] == PackageManager.PERMISSION_GRANTED)){
                Toast.makeText(this, "Se necesita permiso de lectura de memoria", Toast.LENGTH_LONG).show();
            }

            if(!(grantResults[2] == PackageManager.PERMISSION_GRANTED)){
                Toast.makeText(this, "Se necesita permiso de escritura de memoria", Toast.LENGTH_LONG).show();
            }
        }

    }
    public void TomarGuardarFoto (View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, ACTIVITY_CAMARA);

    }

    public void GuardarFoto(){
        if(bitmap != null){

            File archivoFoto = null;
            OutputStream streamSalida = null;

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                ContentResolver resolver = getContentResolver();
                ContentValues values = new ContentValues();

                String nombreArchivo = System.currentTimeMillis()+"_fotoPrueba";

                values.put(MediaStore.Images.Media.DISPLAY_NAME, nombreArchivo);
                values.put(MediaStore.Images.Media.MIME_TYPE, "Image/jpg");
                values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyApp");
                values.put(MediaStore.Images.Media.IS_PENDING, 1);

                Uri coleccion = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
                Uri fotoUri = resolver.insert(coleccion, values);
                String rut=fotoUri.toString();
                uri.setText(rut);

                try{
                    streamSalida = resolver.openOutputStream(fotoUri);
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                }

                values.clear();
                values.put(MediaStore.Images.Media.IS_PENDING, 0);
                resolver.update(fotoUri, values, null, null);
            } else {

                String ruta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                String nombreArchivo = System.currentTimeMillis()+"_fotoPrueba.jpg";
                archivoFoto = new File(ruta, nombreArchivo);
                uri.setText(ruta+"/"+nombreArchivo);
                try{
                    streamSalida = new FileOutputStream(archivoFoto);
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                }

            }

            boolean fotoOk = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, streamSalida);

            if(fotoOk){
                Toast.makeText(this, "Foto Guardada!", Toast.LENGTH_SHORT).show();
            }

            if(streamSalida != null){
                try{
                    streamSalida.flush();
                    streamSalida.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }

            if(archivoFoto != null){
                MediaScannerConnection.scanFile(this, new String[]{archivoFoto.toString()}, null, null);
            }


        } else {
            Toast.makeText(this, "Primero debe tomar una foto antes de usar esta opción", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case ACTIVITY_CAMARA:
                if(resultCode == RESULT_OK){
                    bitmap = (Bitmap) data.getExtras().get("data");
                    foto.setImageBitmap(bitmap);
                    GuardarFoto();

                }
                break;

            case ACTIVITY_GALERIA:
                if(resultCode == RESULT_OK){
                    Uri ruta = data.getData();
                    foto.setImageURI(ruta);
                }
                break;
        }


    }


}