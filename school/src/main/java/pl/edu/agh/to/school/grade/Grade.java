package pl.edu.agh.to.school.grade;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Grade {
    @Id
    @GeneratedValue
    private int id;
    private int gradeValue;
    
}
