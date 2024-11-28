package pl.edu.agh.to.school.grade;

import jakarta.persistence.*;
import pl.edu.agh.to.school.course.Course;
import pl.edu.agh.to.school.student.Student;

@Entity
@Table(name = "grade")
public class Grade {
    @Id
    @GeneratedValue
    @Column(name="id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    private int gradeValue;

    public Grade( int gradeValue, Course course) {
        this.course = course;
        this.gradeValue = gradeValue;
    }
    public Grade() {
    }

    public int getId() {
        return id;
    }

    public Course getCourse() {
        return course;
    }

    public int getGradeValue() {
        return gradeValue;
    }
}
