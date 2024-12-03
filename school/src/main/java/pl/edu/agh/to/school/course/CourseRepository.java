package pl.edu.agh.to.school.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.edu.agh.to.school.student.Student;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository <Course, Integer> {

    @Query("select avg(g.gradeValue) from Course c join c.students s join s.grades g where c.id = :id and g.course.id = :id group by s.id")
    List<Optional<Double>> getAverageGrades(@Param("id") int id);
}
