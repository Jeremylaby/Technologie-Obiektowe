package pl.edu.agh.to.school.course;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import pl.edu.agh.to.school.student.Student;

@Entity
public class Course {
    @Id
    @GeneratedValue
    private int id;
    private String name;

    public Course(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void assignStudent(Student student) {

    }
    public void removeStudent(Student student) {

    }
}

