package com.example.dbreal;

import static android.net.Uri.parse;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adaptador extends RecyclerView.Adapter<Adaptador.ViewHolder> {
    //private FirebaseAuth mAuth;

    private ArrayList<Regalos> regalos;
    public Adaptador(ArrayList<Regalos> regalos){
        this.regalos =regalos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //mAuth=FirebaseAuth.getInstance();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.regalos, parent, false);
        return new ViewHolder(view).enlaceAdapter(this);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(Uri.parse(regalos.get(position).getFoto())).into(holder.foto);
        holder.titulo.setText(regalos.get(position).getTitulo().toString());
        //Log.d("aca",regalos.get(position).getTitulo());
        //holder.idusuario.setText(mAuth.getCurrentUser().toString());
    }

    @Override
    public int getItemCount() {
        return regalos.size();
    }


    public static class ViewHolder extends  RecyclerView.ViewHolder{
        private TextView titulo, idusuario;
        private ImageView foto;
        private Adaptador adaptador;
        private Regalos regalos;

        public ViewHolder(View itemView){
            super(itemView);
            foto=itemView.findViewById(R.id.fotofinal);
            idusuario=itemView.findViewById(R.id.idusuario);
            titulo=itemView.findViewById(R.id.titulo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }
    public ViewHolder enlaceAdapter (Adaptador e){
            this.adaptador = e;
            return this;
    }
    }

}
