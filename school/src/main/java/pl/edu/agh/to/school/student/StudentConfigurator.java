package pl.edu.agh.to.school.student;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;


public class StudentConfigurator {

    CommandLineRunner commandLineRunner(StudentRepository studentRepository) {
        return args -> {
            if (studentRepository.count() == 0) {
                Student kowalski = new Student("Jan", "Kowalski", LocalDate.now(), "123456");
                studentRepository.save(kowalski);
            }
        };
    }
}
