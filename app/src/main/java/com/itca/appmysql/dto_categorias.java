package com.itca.appmysql;

public class dto_categorias {
    int id_categoria;
    String nom_categoria;
    int estado_categoria;

    public dto_categorias() {
    }

    public dto_categorias(int id_categoria, String nom_categoria, int estado_categoria) {
        this.id_categoria = id_categoria;
        this.nom_categoria = nom_categoria;
        this.estado_categoria = estado_categoria;
    }

    public dto_categorias(int id_categoria, String nom_categoria) {
        this.id_categoria = id_categoria;
        this.nom_categoria = nom_categoria;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getNom_categoria() {
        return nom_categoria;
    }

    public void setNom_categoria(String nom_categoria) {
        this.nom_categoria = nom_categoria;
    }

    public int getEstado_categoria() {
        return estado_categoria;
    }

    public void setEstado_categoria(int estado_categoria) {
        this.estado_categoria = estado_categoria;
    }
}
