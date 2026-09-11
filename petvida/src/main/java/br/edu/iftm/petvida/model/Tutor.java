package br.edu.iftm.petvida.model;

public class Tutor {
    private int idTutor;
    private String nome;
    private String telefone;

    public Tutor() {}

    public Tutor(int idTutor, String nome, String telefone) {
        this.idTutor = idTutor;
        this.nome = nome;
        this.telefone = telefone;
    }

    public int getIdTutor() { return idTutor; }
    public void setIdTutor(int idTutor) { this.idTutor = idTutor; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}
