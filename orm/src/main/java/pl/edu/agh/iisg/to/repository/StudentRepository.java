package pl.edu.agh.iisg.to.repository;

import pl.edu.agh.iisg.to.dao.StudentDao;
import pl.edu.agh.iisg.to.model.Course;
import pl.edu.agh.iisg.to.model.Grade;
import pl.edu.agh.iisg.to.model.Student;

import java.util.List;
import java.util.Optional;

public class StudentRepository implements Repository<Student> {
    private StudentDao studentDao;

    public StudentRepository(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public Optional<Student> add(Student student) {
        return studentDao.save(student);
    }

    @Override
    public Optional<Student> getById(int id) {
        return studentDao.findById(id);
    }

    @Override
    public List<Student> findAll() {
        return studentDao.findAll();
    }

    @Override
    public void remove(Student student) {
        transactionService.doAsTransaction(() -> {
            Optional<Student> studentOptional = studentDao.findByIndexNumber(indexNumber);
            if(studentOptional.isEmpty())return false;
//            for(Course course : studentOptional.get().courseSet()) {
//                for( Grade grade : studentOptional.get().gradeSet()) {
//                    if(course.gradeSet().contains(grade)) {
//                        course.gradeSet().remove(grade);
//                    }
//                }
//            }
            for(Course course : studentOptional.get().courseSet()) {
                course.studentSet().remove(studentOptional.get());
            }
            for (Grade grade : studentOptional.get().gradeSet()) {
                gradeDao.remove(grade);
            }
            studentOptional.get().courseSet().clear();
            studentOptional.get().gradeSet().clear();
            studentDao.remove(studentOptional.get());
            return true;
        })
    }
}
