package pl.edu.agh.iisg.to.service;

import pl.edu.agh.iisg.to.dao.CourseDao;
import pl.edu.agh.iisg.to.dao.GradeDao;
import pl.edu.agh.iisg.to.dao.StudentDao;
import pl.edu.agh.iisg.to.model.Course;
import pl.edu.agh.iisg.to.model.Grade;
import pl.edu.agh.iisg.to.model.Student;
import pl.edu.agh.iisg.to.repository.StudentRepository;
import pl.edu.agh.iisg.to.session.TransactionService;

import java.util.*;
import java.util.stream.Collectors;

public class SchoolService {

    private final TransactionService transactionService;

    private final StudentDao studentDao;

    private final CourseDao courseDao;

    private final GradeDao gradeDao;
    private final StudentRepository studentRepository;

    public SchoolService(TransactionService transactionService, StudentDao studentDao, CourseDao courseDao, GradeDao gradeDao, StudentRepository studentRepository) {
        this.transactionService = transactionService;
        this.studentDao = studentDao;
        this.courseDao = courseDao;
        this.gradeDao = gradeDao;
        this.studentRepository=studentRepository;

    }

    public boolean enrollStudent(final Course course, final Student student) {
        return transactionService.doAsTransaction(() -> {
            if(course.studentSet().contains(student)) {
                return false;
            }
           course.studentSet().add(student);
           student.courseSet().add(course);
           return true;
        }).orElse(false);

    }

    public boolean removeStudent(int indexNumber) {
        return transactionService.doAsTransaction(() -> {
            Optional<Student> studentOptional = studentDao.findByIndexNumber(indexNumber);
            if(studentOptional.isEmpty())return false;
//            for(Course course : studentOptional.get().courseSet()) {
//                for( Grade grade : studentOptional.get().gradeSet()) {
//                    if(course.gradeSet().contains(grade)) {
//                        course.gradeSet().remove(grade);
//                    }
//                }
//            }
            studentRepository.remove(studentOptional.get());
            return true;
        }).orElse(false);
    }

    public boolean gradeStudent(final Student student, final Course course, final float gradeValue) {
        return transactionService.doAsTransaction(() -> {
            Grade grade = new Grade(student, course, gradeValue);
            Optional<Grade> savedGrade = gradeDao.save(grade);
            if(savedGrade.isEmpty())return false;
            student.gradeSet().add(grade);
            course.gradeSet().add(grade);

            return true;
        }).orElse(false);
    }

    public Map<String, List<Float>> getStudentGrades(String courseName) {
        Optional<Course>  courseOptional = courseDao.findByName(courseName);
        if(courseOptional.isEmpty())return Collections.emptyMap();
        Course course = courseOptional.get();
        Map<String, List<Float>> studentGrades = new HashMap<>();
        for(Student student : course.studentSet()) {
            List<Float> grades = course.gradeSet().stream()
                    .filter(grade -> grade.student().equals(student)).map(Grade::grade).sorted().toList();
            studentGrades.put(student.firstName() +" "+ student.lastName(), grades);
        }

        return studentGrades;
    }
}
