package com.example.dbreal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private TextView email, password;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.email);
        password=findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser usuarioActual = mAuth.getCurrentUser();
        if(usuarioActual != null)
        {
            Toast.makeText(this,"Existe un usuario activo(correo: "+usuarioActual.getEmail()+")",Toast.LENGTH_LONG).show();
            LanzarActivity();
        }else{
            Toast.makeText(this,"No existe usuario activo",Toast.LENGTH_LONG).show();
        }

    }


    public void login (View view){
        mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                 if(task.isSuccessful()){
                     FirebaseUser usuarioNuevo = mAuth.getCurrentUser();
                     Toast.makeText(MainActivity.this,"Usuario creado: "+usuarioNuevo.getUid(),Toast.LENGTH_LONG).show();
                 }else{
                     Toast.makeText(MainActivity.this,"No se creo la cuenta",Toast.LENGTH_LONG).show();
                 }
            }
        });

    }
    public void crear(View view){
        mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser usuarioNuevo = mAuth.getCurrentUser();
                    Toast.makeText(MainActivity.this,"Usuario creado "+usuarioNuevo.getUid(),Toast.LENGTH_LONG).show();
                    LanzarActivity();
                }else{
                    Toast.makeText(MainActivity.this,"No se pudo crear la cuenta", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void LanzarActivity(){
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }
}