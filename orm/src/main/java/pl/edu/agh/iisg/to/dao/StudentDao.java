package pl.edu.agh.iisg.to.dao;

import jakarta.persistence.PersistenceException;
import pl.edu.agh.iisg.to.model.Student;
import pl.edu.agh.iisg.to.session.SessionService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StudentDao extends GenericDao<Student> {

    public StudentDao(SessionService sessionService) {
        super(sessionService, Student.class);
    }

    public Optional<Student> create(final String firstName, final String lastName, final int indexNumber) {
        Student student = new Student(firstName,lastName,indexNumber);
        return save(student);
    }

    public List<Student> findAll() {
        try{
            return currentSession().createQuery("SELECT s FROM Student s order by lastName",Student.class).getResultList();
        }catch (PersistenceException e){
            return Collections.emptyList();
        }

    }

    public Optional<Student> findByIndexNumber(final int indexNumber) {
        try{return currentSession().createQuery("SELECT s FROM Student s WHERE s.indexNumber = :index",Student.class)
                .setParameter("index",indexNumber).uniqueResultOptional();
        }catch(PersistenceException e){
            return Optional.empty();
        }
    }
}
