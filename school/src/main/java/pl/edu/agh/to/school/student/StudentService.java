package pl.edu.agh.to.school.student;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.school.course.Course;
import pl.edu.agh.to.school.course.CourseService;
import pl.edu.agh.to.school.grade.Grade;
import pl.edu.agh.to.school.grade.GradeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseService courseService;
    private final GradeRepository gradeRepository;

    public StudentService(StudentRepository studentRepository, CourseService courseService, GradeRepository gradeRepository) {
        this.studentRepository = studentRepository;
        this.courseService = courseService;
        this.gradeRepository = gradeRepository;
    }
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }
    public Optional<Student> getStudent(int id) {
        return studentRepository.findById(id);
    }
    public Optional<Student> getStudentByIndexNumber(String indexNumber){
        return studentRepository.findByIndexNumber(indexNumber);
    }
    @Transactional
    public Optional<Grade> giveGrade(int id, int value, int courseId){
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isEmpty()) {
            return Optional.empty();
        }
        Student student = studentOptional.get();

        Optional<Course> courseOptional = courseService.getCourseById(courseId);
        if (courseOptional.isEmpty()) {
            return Optional.empty();
        }
        Course course = courseOptional.get();

        Grade grade = new Grade(value, course);
        gradeRepository.save(grade);
        student.giveGrade(grade);
        studentRepository.save(student);
        return Optional.of(grade);

    }
    public Optional<Double> getAverageGrade(int id){
        return studentRepository.getAverageGrade(id);
    }
}