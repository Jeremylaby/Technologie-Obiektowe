package pl.edu.agh.to.school.grade;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import pl.edu.agh.to.school.course.Course;


@Entity
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int gradeValue;

    @ManyToOne
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
