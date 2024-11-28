package pl.edu.agh.to.school.course;

import jakarta.persistence.*;
import pl.edu.agh.to.school.grade.Grade;
import pl.edu.agh.to.school.student.Student;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int id;
    private String name;
    @ManyToMany()
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "ID"))
    private final Set<Student> students = new HashSet<>();

    public Course( String name) {
        this.name = name;
    }

    public Course() {

    }
    public void assignStudent(Student student) {
        students.add(student);
    }
    public void removeStudent(Student student) {
        students.remove(student);
    }
    public int getId() {
        return id;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public String getName() {
        return name;
    }

}

