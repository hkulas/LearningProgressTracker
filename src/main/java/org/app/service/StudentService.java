package org.app.service;

import org.app.model.Course;
import org.app.model.Student;
import org.app.validator.Validator;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

public class StudentService {
    private Validator validator;

    public StudentService(Validator validator) {
        this.validator = validator;
    }

    public void addStudents(Set<Student> students, Scanner scanner) {
        System.out.println("Enter student credentials or 'back' to return");
        String input = scanner.nextLine();
        int counter = 0;

        while (!input.equals("back")) {
            Optional<Student> student = validator.checkCredentials(input, scanner, students);
            if (student.isPresent()) {
                if (!students.add(student.get())) {
                    System.out.println("This email is already taken.");
                } else {
                    System.out.println("Student has been added");
                    counter++;
                }
                input = scanner.nextLine();
            } else {
                break;
            }
        }

        System.out.println("Total " + counter + " students have been added.");
    }

    public void addPoints(Set<Student> students, Scanner scanner) {
        System.out.println("Enter an id and points or 'back' to return: ");
        String input = scanner.nextLine();
        validator.checkCoursesInput(input, scanner, students);
    }


    public void findStudentById(Set<Student> students, Scanner scanner) {
        System.out.println("Enter an id or 'back' to return");
        String id = scanner.nextLine();
        while (!id.equals("back")) {
            if (!validator.isNumeric(id)) {
                System.out.printf("No student is found for id=%s\n", id);
                id = scanner.nextLine();
                continue;
            }
            String inputFinal = id;
            Optional<Student> student = students.stream()
                    .filter(s -> s.getId().equals(Long.parseLong(inputFinal))).findFirst();

            if (!student.isPresent()) {
                System.out.printf("No student is found for id=%s\n", id);
                id = scanner.nextLine();
                continue;
            }

            List<Course> courses = student.get().getCourses();
            int java = courses.get(0).getPoints();
            int dsa = courses.get(1).getPoints();
            int database = courses.get(2).getPoints();
            int spring = courses.get(3).getPoints();
            System.out.printf("%s points: Java=%d; DSA=%d; Databases=%d; Spring=%d", id, java, dsa, database, spring);
            id = scanner.nextLine();

        }
    }
}
