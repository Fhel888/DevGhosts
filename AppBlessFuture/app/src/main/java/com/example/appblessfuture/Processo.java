package com.example.appblessfuture;

public class Processo {
    private int id;
    private String nome;
    private String setor;
    private String arquivo;
    private String data;

    // Gets
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getSetor() { return setor; }
    public String getArquivo() { return arquivo; }
    public String getData() { return data; }

    // Sets
    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setSetor(String setor) { this.setor = setor; }
    public void setArquivo(String arquivo) { this.arquivo = arquivo; }
    public void setData(String data) { this.data = data; }
}