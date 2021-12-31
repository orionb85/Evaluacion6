package com.example.dbreal;

import java.io.Serializable;

public class Regalos implements Serializable {
    private String titulo,foto;

    public Regalos(){
        titulo="";
        foto="";
    }
    public Regalos (String titulo, String foto){
        this.titulo=titulo;
        this.foto=foto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
