package org.app.validator;

import org.app.model.Course;
import org.app.model.Student;

import java.util.*;
import java.util.regex.Pattern;

public class Validator {
    public Optional<Student> checkCredentials(String input, Scanner scanner, Set<Student> students) {
        String[] parts = input.split(" ");

        while (!input.equals("back")) {
            if (parts.length < 3) {
                System.out.println("Incorrect credentials.");
                input = scanner.nextLine();
                parts = input.split(" ");
                continue;
            }

            String firstName = parts[0];
            String lastName = parts[parts.length - 2];
            String email = parts[parts.length - 1];

            if (!isValidName(firstName)) {
                System.out.println("Incorrect first name.");
                input = scanner.nextLine();
                parts = input.split(" ");
                continue;
            }

            if (!isValidName(lastName)) {
                System.out.println("Incorrect last name.");
                input = scanner.nextLine();
                parts = input.split(" ");
                continue;
            }

            if (!isValidEmail(email)) {
                System.out.println("Incorrect email.");
                input = scanner.nextLine();
                parts = input.split(" ");
                continue;
            }
            Long id = students.size() + 1L;
            return Optional.of(new Student(id, firstName, lastName, email));
        }

        return Optional.empty();
    }


    public boolean isValidName(String name) {
        String regex = "^(?![-'])(?=[-'A-Za-z]{2})(?=[A-Za-z])[A-Za-z]+(?:[-']?[A-Za-z]+)*(?<![-'\\s])$";
        return name.matches(regex);
    }

    public boolean isValidEmail(String email) {
        String regex = "[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+";

        return email.matches(regex);
    }

    public void checkCoursesInput(String input, Scanner scanner, Set<Student> students) {
        final String INCORRECT_POINTS_FORMAT = "Incorrect points format";
        String[] parts = input.split(" ");
        Optional<Student> student = Optional.empty();
        while (!input.equals("back")) {
            if (parts.length != 5) {
                System.out.println(INCORRECT_POINTS_FORMAT);
                input = scanner.nextLine();
                parts = input.split(" ");
                continue;
            }

            if (!isNumeric(parts[0])) {
                System.out.printf("No student found for id=%s\n", parts[0]);
                input = scanner.nextLine();
                parts = input.split(" ");
                continue;
            }

            if (!Arrays.stream(parts).allMatch(this::isNumeric)) {
                System.out.println(INCORRECT_POINTS_FORMAT);
                input = scanner.nextLine();
                parts = input.split(" ");
                continue;
            }

            Long id = Long.parseLong(parts[0]);
            int java = Integer.parseInt(parts[1]);
            int dataStructures = Integer.parseInt(parts[2]);
            int databases = Integer.parseInt(parts[3]);
            int spring = Integer.parseInt(parts[4]);

            if (java < 0 || dataStructures < 0 || databases < 0 || spring < 0) {
                System.out.println(INCORRECT_POINTS_FORMAT);
                input = scanner.nextLine();
                parts = input.split(" ");
                continue;
            }

            if (students.stream().noneMatch(e -> e.getId().equals(id))) {
                System.out.printf("No student is found for id=%s\n", id);
                input = scanner.nextLine();
                parts = input.split(" ");
                continue;
            }

            student = students.stream().filter(s -> s.getId().equals(id))
                    .findFirst();
            List<Course> courses = student.get().getCourses();
            addPointsToCourses(courses, java, dataStructures, databases, spring);
            student.get().setCourses(courses);
            System.out.println("Points updated.");

            input = scanner.nextLine();
            parts = input.split(" ");
        }

    }

    private void addPointsToCourses(List<Course> courses, int java, int dataStructures, int databases, int spring) {
        int[] points = {java, dataStructures, databases, spring};

        for (int i = 0; i < courses.size(); i++) {
            int currentPoints = points[i];
            Course currentCourse = courses.get(i);

            currentCourse.setPoints(currentPoints + currentCourse.getPoints());

            if (currentPoints > 0) {
                if (currentCourse.getPopularity() == 0) {
                    currentCourse.setPopularity(1);
                }
                currentCourse.setActivity(currentCourse.getActivity() + 1);
                currentCourse.setAverageScore((currentCourse.getAverageScore() + currentCourse.getPoints()) / 2);
            } else if (currentPoints == 0) {
                currentCourse.setAverageScore(currentCourse.getPoints());
            }
        }

    }

    public boolean isNumeric(String str) {
        final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        return pattern.matcher(str).matches();
    }
}
