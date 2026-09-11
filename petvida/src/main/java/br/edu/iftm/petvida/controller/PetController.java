package br.edu.iftm.petvida.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.edu.iftm.petvida.model.Animal;
import br.edu.iftm.petvida.repository.AnimalRepository;
import br.edu.iftm.petvida.repository.TutorRepository;

@Controller
public class PetController {

    private final AnimalRepository animalRepo;
    private final TutorRepository tutorRepo;

    public PetController(AnimalRepository animalRepo, TutorRepository tutorRepo) {
        this.animalRepo = animalRepo;
        this.tutorRepo = tutorRepo;
    }

    @GetMapping("/ficha_37")
    public String ficha(Model model) {
        Animal animal = animalRepo.buscarPorId(137); // ID semente (100+NN)
        model.addAttribute("nomeAnimal", animal.getNome());
        model.addAttribute("especie", animal.getEspecie());
        model.addAttribute("idade", String.valueOf(animal.getIdade()));
        model.addAttribute("nomeTutor", animal.getTutor().getNome());
        model.addAttribute("telefoneTutor", animal.getTutor().getTelefone());
        return "ficha";
    }

    @GetMapping("/tutor_37")
    public String tutor(Model model) {
        Animal animal = animalRepo.buscarPorId(137);
        int qtdAnimais = tutorRepo.contarAnimaisDoTutor(137);
        model.addAttribute("nomeTutor", animal.getTutor().getNome());
        model.addAttribute("telefoneTutor", animal.getTutor().getTelefone());
        model.addAttribute("qtdAnimais", String.valueOf(qtdAnimais));
        return "tutor";
    }

    @GetMapping("/resumo_37")
    public String resumo(Model model) {
        int total = animalRepo.contarAnimais();
        double media = animalRepo.mediaIdade();
        String maisVelho = animalRepo.animalMaisVelho();
        
        String dataHoraFormatted = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        String mediaFormatted = String.format("%.2f", media);

        model.addAttribute("totalAnimais", String.valueOf(total));
        model.addAttribute("mediaIdade", mediaFormatted);
        model.addAttribute("maisVelho", maisVelho);
        model.addAttribute("dataHora", dataHoraFormatted);
        return "resumo";
    }
}