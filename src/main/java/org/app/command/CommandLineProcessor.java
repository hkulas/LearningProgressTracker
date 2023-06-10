package org.app.command;

import org.app.model.Student;
import org.app.service.NotificationService;
import org.app.service.StatisticsService;
import org.app.service.StudentService;

import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

public class CommandLineProcessor {
    public static Set<Student> students = new LinkedHashSet<>();
    private StudentService studentService;
    private StatisticsService statisticsService;

    private NotificationService notificationService;

    public CommandLineProcessor(StudentService studentService, StatisticsService statisticsService, NotificationService notificationService) {
        this.studentService = studentService;
        this.statisticsService = statisticsService;
        this.notificationService = notificationService;
    }

    public void processInput() {
        Scanner scanner = new Scanner(System.in);
        boolean flag = false;
        while (!flag) {
            String line = scanner.nextLine().trim();
            switch (line) {
                case "exit" -> {
                    System.out.println("Bye!");
                    flag = true;
                }
                case "" -> System.out.println("No input.");
                case "back" -> System.out.println("Enter 'exit' to exit the program.");
                case "list" -> list(students);
                case "add students" -> this.studentService.addStudents(students, scanner);
                case "add points" -> this.studentService.addPoints(students, scanner);
                case "find" -> this.studentService.findStudentById(students, scanner);
                case "statistics" -> this.statisticsService.showStatistics(students, scanner);
                case "notify" -> this.notificationService.notify(students, scanner);
                default -> System.out.println("Unknown command!");
            }
        }
        scanner.close();
    }

    private void list(Set<Student> students) {
        if (students.size() > 0) {
            System.out.println("Students:");
            students.forEach(System.out::println);
        } else {
            System.out.println("No students found.");
        }
    }
}
