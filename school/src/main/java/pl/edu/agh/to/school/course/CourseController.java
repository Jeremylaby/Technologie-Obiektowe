package pl.edu.agh.to.school.course;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.school.student.Student;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }
    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }
    @GetMapping("/{id}/students")
    public List<Student> getCourseById(@PathVariable int id) {
        return courseService.getCourseById(id).map(Course::getStudents).orElseThrow( ()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    @GetMapping("/{id}/grades")
    public List<CourseGd>getGrades(@PathVariable int id){

            return courseService.getCourseById(id)
                    .map(course -> course.getStudents()
                            .stream()
                            .flatMap(student -> student.getGrades()
                                    .stream()
                                    .filter(grade -> grade.getCourse().equals(course))
                                    .map(grade -> new CourseGd(grade.getGradeValue(), student.getId())))
                            .toList())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    @GetMapping("/{id}/grades/avg")
    public List<Double> getAverageGrades(@PathVariable int id) {
        return courseService.getAverageGrades(id)
                .stream()
                .flatMap(Optional::stream)
                .toList();
    }
    public record CourseGd(int grade, int studentId){}
}
