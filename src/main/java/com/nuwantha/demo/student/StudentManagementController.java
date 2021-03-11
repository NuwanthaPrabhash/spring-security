package com.nuwantha.demo.student;

import com.nuwantha.demo.security.ApplicationUserRoles;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path = "management/api")
public class StudentManagementController {

    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1, "nuwantha prabhash"),
            new Student(2, "sjdhfs sdjfhgsjb"),
            new Student(3, "ffffffffff ffffff")
    );

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMIN_TRAINEE')")
    public List<Student> getAllStudents() {
        System.out.println("getAllStudents");
        return STUDENTS;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('student:write')")
    public void registerNewStudent(@RequestBody Student student) {
        System.out.println("Register new students");
        System.out.println(student);
    }

    @DeleteMapping(path = "{studentId}")
    @PreAuthorize("hasAuthority('student:write')")
    public void deleteStudent(@PathVariable("studentId") Integer studentId) {
        System.out.println("delete student");
        System.out.println(studentId);
    }

    @PutMapping(path = "{studentId}")
    @PreAuthorize("hasAuthority('student:write')")
    public void studentUpdate(@PathVariable("studentId") Integer studentId, @RequestBody Student student) {
        System.out.println("update student");
        System.out.println(String.format("%s %s", studentId, student ));
    }


}
