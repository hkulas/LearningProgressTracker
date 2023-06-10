package org.app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Student {
    private Long id;
    private String firstName;
    private String lastName;

    private String email;

    private boolean sendNotification;

    private List<Course> courses = new ArrayList<>();

    public Student(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.sendNotification = false;
        courses.add(new Course(Course.CourseName.JAVA, 0, 600, 0, 0, 0));
        courses.add(new Course(Course.CourseName.DATA_STRUCTURES_AND_ALGORITHMS, 0, 400, 0, 0, 0));
        courses.add(new Course(Course.CourseName.DATABASES, 0, 480, 0, 0, 0));
        courses.add(new Course(Course.CourseName.SPRING, 0, 550, 0, 0, 0));
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public boolean isSendNotification() {
        return sendNotification;
    }

    public void setSendNotification(boolean sendNotification) {
        this.sendNotification = sendNotification;
    }

    @Override
    public String toString() {
        return id +
                " " + (courses.get(0).getPoints() != 0 ? courses.get(0).getPoints() : "") +
                " " + (courses.get(1).getPoints() != 0 ? courses.get(1).getPoints() : "") +
                " " + (courses.get(2).getPoints() != 0 ? courses.get(2).getPoints() : "") +
                " " + (courses.get(3).getPoints() != 0 ? courses.get(3).getPoints() : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(email, student.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
