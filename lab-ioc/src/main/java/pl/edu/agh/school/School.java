package pl.edu.agh.school;

import com.google.inject.Inject;
import pl.edu.agh.logger.Logger;
import pl.edu.agh.school.persistence.SerializablePersistenceManager;

import java.util.ArrayList;
import java.util.Collection;

public class School {

    private SchoolDAO schoolDAO;
    private final Logger logger;
    @Inject
    public School(SchoolDAO schoolDAO, Logger logger) {
        this.schoolDAO = schoolDAO;
        this.logger = logger;
    }

    public void addTeacher(Teacher teacher) {
        logger.log("Teacher added to DAO: " + teacher);
        schoolDAO.addTeacher(teacher);
    }

    public Collection<Person> findPerson(String name, String surname) {
        Collection<Person> foundPersons = findPerson(name, surname,
                PersonType.Student);
        foundPersons.addAll(findPerson(name, surname, PersonType.Teacher));
        logger.log("Found " + foundPersons.size() + " for :" +name+" "+surname);
        return foundPersons;
    }

    public Collection<Person> findPerson(String name, String surname,
                                         PersonType personType) {
        ArrayList<Person> foundTeachers = new ArrayList<>();
        if (personType == PersonType.Teacher) {
            for (Teacher teacher : schoolDAO.getTeachers()) {
                if (teacher.meetsSearchCriteria(name, surname)) {
                    foundTeachers.add(teacher);
                }
            }
        } else if (personType == PersonType.Student) {
            for (SchoolClass schoolClass : schoolDAO.getClasses()) {
                for (Student student : schoolClass.getStudents()) {
                    if (student.meetsSearchCriteria(name, surname)) {
                        foundTeachers.add(student);
                    }
                }
            }
        }
        logger.log("Found " + foundTeachers.size() + " persons in " + personType);
        return foundTeachers;
    }

    public void addClass(SchoolClass newClass) {
        logger.log("Class added to DAO: " + newClass);
        schoolDAO.addClass(newClass);
    }

    public Collection<SchoolClass> findClass(String name, String profile) {
        ArrayList<SchoolClass> foundClasses = new ArrayList<>();
        for (SchoolClass schoolClass : schoolDAO.getClasses()) {
            if (schoolClass.meetSearchCriteria(name, profile)) {
                foundClasses.add(schoolClass);
            }
        }
        logger.log("Found " + foundClasses.size() + " classes in " + profile+ " "+name);
        return foundClasses;
    }

    public int getTermCount(Person person) {
        return person.getSchedule().size();
    }
}
