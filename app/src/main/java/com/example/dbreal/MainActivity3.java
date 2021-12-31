package com.example.dbreal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends AppCompatActivity {
    private RecyclerView recycler;
    private Adaptador adaptador;
    private ArrayList<Regalos> regalos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        regalos = (ArrayList<Regalos>) getIntent().getSerializableExtra("regalos");

        Log.d("aca1",String.valueOf(regalos.size()) );

       adaptador = new Adaptador(regalos);

        recycler=findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(MainActivity3.this));
        recycler.setAdapter(adaptador);
    }
}