package com.example.project_duo.Others;

public class FileInfoPortal {

    private String titulo;
    private String qtd_itens;
    private int icone;

    public FileInfoPortal() {
    }

    public FileInfoPortal(String ti, String qtd, int ic) {
        titulo = ti;
        qtd_itens = qtd;
        icone = ic;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getQtd_itens() {
        return qtd_itens;
    }

    public void setQtd_itens(String qtd_itens) {
        this.qtd_itens = qtd_itens;
    }

    public int getIcone() {
        return icone;
    }

    public void setIcone(int icone) {
        this.icone = icone;
    }

}
