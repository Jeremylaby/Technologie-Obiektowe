package pl.edu.agh.to.school.grade;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import pl.edu.agh.to.school.course.Course;


@Entity
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int gradeValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Course course;

    public Grade(int value, Course course) {
        this.gradeValue = value;
        this.course = course;
    }

    public Grade() {}

    public int getId() {
        return id;
    }

    public int getGradeValue() {
        return gradeValue;
    }

    public Course getCourse() {
        return course;
    }
}
