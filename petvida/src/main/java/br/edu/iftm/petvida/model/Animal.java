package br.edu.iftm.petvida.model;

public class Animal {
    private int idAnimal;
    private String nome;
    private String especie;
    private int idade;
    private Tutor tutor;

    public Animal() {}

    public Animal(int idAnimal, String nome, String especie, int idade, Tutor tutor) {
        this.idAnimal = idAnimal;
        this.nome = nome;
        this.especie = especie;
        this.idade = idade;
        this.tutor = tutor;
    }

    public int getIdAnimal() { return idAnimal; }
    public void setIdAnimal(int idAnimal) { this.idAnimal = idAnimal; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }
    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade = idade; }
    public Tutor getTutor() { return tutor; }
    public void setTutor(Tutor tutor) { this.tutor = tutor; }
}
