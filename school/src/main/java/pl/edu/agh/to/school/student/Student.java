package pl.edu.agh.to.school.student;

import jakarta.persistence.*;
import pl.edu.agh.to.school.course.Course;
import pl.edu.agh.to.school.grade.Grade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String indexNumber;

    @ManyToMany(mappedBy = "students")
    private Set<Course> courses = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Grade> grades = new ArrayList<>();
    public Student( String firstName, String lastName, LocalDate birthDate, String indexNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.indexNumber = indexNumber;
    }

    public Student() {

    }

    public void giveGrade(Grade grade){
        grades.add(grade);
    }


    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getIndexNumber() {
        return indexNumber;
    }
}
