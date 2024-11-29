package pl.edu.agh.to.school.student;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path = "students")
public class StudentController {
    private final StudentService studentService;
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getStudent(@RequestParam(value= "indexNumber", required = false) String indexNumber) {
        if(indexNumber != null) {
            return studentService
                    .getStudentByIndexNumber(indexNumber)
                    .map(List::of)
                    .orElseGet(Collections::emptyList);
        }
        return studentService.getStudents();
    }
    @PostMapping("/{id}/grades")
    public GradeDt giveGrade(@PathVariable int id, @RequestBody GradeDt gradeBody) {
        return studentService.giveGrade(id, gradeBody.grade,gradeBody.courseId)
                .map(grade -> new GradeDt(grade.getGradeValue(),grade.getCourse().getId()))
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    @GetMapping("{id}/grades")
    public List<GradeDt> getGrades(@PathVariable int id) {
        return studentService.getStudent(id)
                .map(student -> student.getGrades()
                        .stream()
                        .map(grade -> new GradeDt(grade.getGradeValue(),grade.getCourse().getId()))
                        .toList())

                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


    }
    @GetMapping("/{id}/grades/avg")
    public Double getAverageGrade(@PathVariable int id) {
        return studentService.getAverageGrade(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    private record GradeDt(int grade , int courseId){

    }
}
