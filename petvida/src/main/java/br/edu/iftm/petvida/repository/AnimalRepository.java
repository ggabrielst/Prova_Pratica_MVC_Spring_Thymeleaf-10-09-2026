package br.edu.iftm.petvida.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import br.edu.iftm.petvida.model.Animal;
import br.edu.iftm.petvida.model.Tutor;

@Repository
public class AnimalRepository {

    private final JdbcTemplate jdbc;

    public AnimalRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void salvar(Animal animal) {
        String sql = "INSERT INTO animal (id_animal, nome, especie, idade, tutor_id_tutor) VALUES (?, ?, ?, ?, ?)";
        jdbc.update(sql, animal.getIdAnimal(), animal.getNome(), animal.getEspecie(), animal.getIdade(), animal.getTutor().getIdTutor());
    }

    public Animal buscarPorId(int id) {
        String sql = "SELECT a.id_animal, a.nome AS animal_nome, a.especie, a.idade, " +
                     "t.id_tutor, t.nome AS tutor_nome, t.telefone " +
                     "FROM animal a JOIN tutor t ON a.tutor_id_tutor = t.id_tutor " +
                     "WHERE a.id_animal = ?";
        
        return jdbc.queryForObject(sql, (rs, rowNum) -> {
            Tutor tutor = new Tutor(
                rs.getInt("id_tutor"),
                rs.getString("tutor_nome"),
                rs.getString("telefone")
            );
            return new Animal(
                rs.getInt("id_animal"),
                rs.getString("animal_nome"),
                rs.getString("especie"),
                rs.getInt("idade"),
                tutor
            );
        }, id);
    }

    public int contarAnimais() {
        String sql = "SELECT COUNT(*) FROM animal";
        return jdbc.queryForObject(sql, Integer.class);
    }

    public double mediaIdade() {
        // CAST para Double evita truncamento de inteiros no H2
        String sql = "SELECT AVG(CAST(idade AS DOUBLE)) FROM animal";
        Double media = jdbc.queryForObject(sql, Double.class);
        return media != null ? media : 0.0;
    }

    public String animalMaisVelho() {
        String sql = "SELECT nome FROM animal ORDER BY idade DESC LIMIT 1";
        return jdbc.queryForObject(sql, String.class);
    }
}