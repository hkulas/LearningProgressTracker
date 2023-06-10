package org.app.service;

import org.app.model.Course;
import org.app.model.Student;

import java.util.HashSet;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

public class NotificationService {
    public void notify(Set<Student> students, Scanner scanner) {
        Set<String> notifiedStudents = new HashSet<>();

        for (Student student : students) {
            if (!student.isSendNotification()) {
                checkAndNotifyCourse(student, Course.CourseName.JAVA, notifiedStudents);
                checkAndNotifyCourse(student, Course.CourseName.DATA_STRUCTURES_AND_ALGORITHMS, notifiedStudents);
                checkAndNotifyCourse(student, Course.CourseName.DATABASES, notifiedStudents);
                checkAndNotifyCourse(student, Course.CourseName.SPRING, notifiedStudents);
            }
        }


        System.out.printf("Total %d students have been notified.", notifiedStudents.size());

    }

    private void checkAndNotifyCourse(Student student, Course.CourseName courseName, Set<String> notifiedStudents) {
        Optional<Course> course = extractCourse(student, courseName);
        course.ifPresent(c -> {
            if (!c.isCompleted() && c.getPoints() >= c.getLimit()) {
                sendNotification(c, student);
                notifiedStudents.add(student.getEmail());
            }
        });
    }

    private void sendNotification(Course course, Student student) {
        int totalCoursePoints = course.getPoints();
        if (totalCoursePoints >= course.getLimit()) {
            course.setCompleted(true);
            System.out.printf("To: %s\n", student.getEmail());
            System.out.println("Re: Your Learning Progress");
            String fullName = student.getFirstName() + " " + student.getLastName();
            System.out.printf("Hello, %s! You have accomplished our %s course!\n", fullName, course.getName().getName());
        }

    }

    private Optional<Course> extractCourse(Student student, Course.CourseName courseName) {
        return student.getCourses().stream()
                .filter(course -> course.getName() == courseName)
                .findFirst();
    }
}
