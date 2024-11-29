package pl.edu.agh.to.school.course;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.edu.agh.to.school.student.Student;

import java.util.List;

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

}
