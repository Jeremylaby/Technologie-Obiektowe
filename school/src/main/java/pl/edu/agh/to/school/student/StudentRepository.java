package pl.edu.agh.to.school.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
 Optional<Student> findByIndexNumber(String id);


 @Query("select avg(g.gradeValue) from Student s join s.grades g where s.id = :id")
 Optional<Double> getAverageGrade(@Param("id") int id);
}
