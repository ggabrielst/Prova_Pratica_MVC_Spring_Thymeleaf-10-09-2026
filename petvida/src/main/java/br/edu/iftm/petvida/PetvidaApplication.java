package br.edu.iftm.petvida;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.edu.iftm.petvida.model.Animal;
import br.edu.iftm.petvida.model.Tutor;
import br.edu.iftm.petvida.repository.AnimalRepository;
import br.edu.iftm.petvida.repository.TutorRepository;

@SpringBootApplication
public class PetvidaApplication implements CommandLineRunner {

    private final TutorRepository tutorRepo;
    private final AnimalRepository animalRepo;

    public PetvidaApplication(TutorRepository tutorRepo, AnimalRepository animalRepo) {
        this.tutorRepo = tutorRepo;
        this.animalRepo = animalRepo;
    }

    public static void main(String[] args) {
        SpringApplication.run(PetvidaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Dados iniciais padrões da prova
        Tutor t1 = new Tutor(1, "Marina Alves", "34 99101-0001");
        Tutor t2 = new Tutor(2, "Carlos Prado", "34 99101-0002");
        tutorRepo.salvar(t1);
        tutorRepo.salvar(t2);

        animalRepo.salvar(new Animal(2, "Mimi", "gato", 3, t1));
        animalRepo.salvar(new Animal(3, "Thor", "cao", 1, t2));
        animalRepo.salvar(new Animal(4, "Lila", "gato", 11, t2));

        // Semente de personalização (Substitua NN=34 pelas suas credenciais)
        int nn = 37;
        int myId = 100 + nn;
        Tutor meuTutor = new Tutor(myId, "Gabriel", "34 9" + nn + nn + "-" + nn + nn);
        tutorRepo.salvar(meuTutor);

        Animal meuAnimal = new Animal(myId, "Orianna_" + nn, "gato", nn, meuTutor);
        animalRepo.salvar(meuAnimal);
    }
}