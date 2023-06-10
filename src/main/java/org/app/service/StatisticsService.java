package org.app.service;

import org.app.model.Course;
import org.app.model.Student;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StatisticsService {
    public void showStatistics(Set<Student> students, Scanner scanner) {
        System.out.println("Type the name of a course to see details or 'back' to quit");
        showMostPopularAndLeastPopularCourse(students);
        showHighestActivityAndLowestActivity(students);
        showEasiestAndHardestCourse(students);
        String input = scanner.nextLine();

        while (!input.equals("back")) {
            String finalInput = input;
            boolean containsCourse = Arrays.stream(Course.CourseName.values())
                    .anyMatch(c -> c.getName().equalsIgnoreCase(finalInput));

            if (containsCourse) {
                showCourseRanking(students, input);
            } else {
                System.out.println("Unknown course.");
            }
            input = scanner.nextLine();
        }

    }

    private void showCourseRanking(Set<Student> students, String courseName) {
        List<Student> sortedStudents = students.stream()
                .sorted(Comparator.comparingInt((Student s) ->
                                s.getCourses().stream()
                                        .filter(c -> c.getName().getName().equalsIgnoreCase(courseName))
                                        .mapToInt(Course::getPoints)
                                        .sum())
                        .reversed()
                        .thenComparingLong(Student::getId))
                .toList();
        Course.CourseName cousreOfficialName = Arrays.stream(Course.CourseName.values())
                .filter(c -> c.getName().equalsIgnoreCase(courseName))
                .findFirst()
                .orElse(null);

        System.out.println(cousreOfficialName.getName());
        System.out.println("id    points    completed");
        if (sortedStudents.isEmpty()) {
            System.out.println("n/a      n/a       n/a");
            return;
        }

        for (Student student : sortedStudents) {
            int totalPoints = student.getCourses().stream()
                    .filter(c -> c.getName().getName().equals(courseName))
                    .mapToInt(Course::getPoints)
                    .sum();
            Optional<Course> optionalCourse = student.getCourses().stream()
                    .filter(c -> c.getName().getName().equals(courseName))
                    .findFirst();

            BigDecimal bigDecimal = new BigDecimal((double) totalPoints / optionalCourse.get().getLimit()).setScale(3, RoundingMode.HALF_UP).scaleByPowerOfTen(2);
            if (totalPoints != 0) {
                System.out.println(student.getId() + " " + totalPoints + " " + bigDecimal + "%");

            }
        }

    }

    private void showMostPopularAndLeastPopularCourse(Set<Student> students) {
        Map<Course.CourseName, Long> popularityCount = students.stream()
                .filter(distinctByKey(Student::getId))
                .flatMap(s -> s.getCourses().stream())
                .collect(Collectors.groupingBy(Course::getName, Collectors.summingLong(c -> c.getPopularity() > 0 ? 1L : 0L)));

        OptionalLong maxPopularity = popularityCount.values().stream()
                .mapToLong(Long::valueOf)
                .max();

        List<String> mostPopularCourses = popularityCount.entrySet().stream()
                .filter(e -> e.getValue().equals(maxPopularity.orElse(0L)))
                .map(e -> e.getKey().getName())
                .collect(Collectors.toList());

        String mostPopularCoursesResult = mostPopularCourses.isEmpty() ? "n/a" : String.join(", ", mostPopularCourses);
        System.out.printf("Most popular: %s\n", mostPopularCoursesResult);

        OptionalLong minPopularity = popularityCount.values().stream()
                .mapToLong(Long::valueOf)
                .min();

        List<String> leastPopularCourses = popularityCount.entrySet().stream()
                .filter(e -> !mostPopularCourses.contains(e.getKey().getName()))
                .filter(e -> e.getValue().equals(minPopularity.orElse(0L)))
                .map(e -> e.getKey().getName())
                .collect(Collectors.toList());
        String leastPopularCoursesResult = leastPopularCourses.isEmpty() ? "n/a" : String.join(", ", leastPopularCourses);
        System.out.printf("Least popular: %s\n", String.join(", ", leastPopularCoursesResult));
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }


    private void showHighestActivityAndLowestActivity(Set<Student> students) {
        Map<Course.CourseName, Long> activityCount = students.stream()
                .flatMap(s -> s.getCourses().stream())
                .collect(Collectors.groupingBy(Course::getName, Collectors.summingLong(Course::getActivity)));

        Optional<Long> maxActivityCount = activityCount.values().stream()
                .max(Long::compare);

        List<String> highestActivityCourses = activityCount.entrySet().stream()
                .filter(e -> e.getValue().equals(maxActivityCount.orElse(0L)))
                .map(e -> e.getKey().getName())
                .collect(Collectors.toList());
        String highestActivityCoursesResult = highestActivityCourses.isEmpty() ? "n/a" : String.join(", ", highestActivityCourses);
        System.out.printf("Highest activity %s\n", highestActivityCoursesResult);

        Optional<Long> minActivityCount = activityCount.values().stream()
                .min(Long::compare);

        List<String> lowestActivityCourses = activityCount.entrySet().stream()
                .filter(e -> e.getValue().equals(minActivityCount.orElse(0L)))
                .filter(e -> !highestActivityCourses.contains(e.getKey().getName()))
                .map(e -> e.getKey().getName())
                .collect(Collectors.toList());

        String lowestActivityCoursesResult = lowestActivityCourses.isEmpty() ? "n/a" : String.join(", ", lowestActivityCourses);
        System.out.printf("Lowest activity %s\n", lowestActivityCoursesResult);
    }

    private void showEasiestAndHardestCourse(Set<Student> students) {
        Map<Course.CourseName, Double> ratioMap = students.stream()
                .flatMap(s -> s.getCourses().stream())
                .collect(Collectors.groupingBy(
                        Course::getName,
                        Collectors.collectingAndThen(
                                Collectors.reducing(
                                        (c1, c2) -> new Course(
                                                c1.getName(),
                                                c1.getPoints() + c2.getPoints(),
                                                c1.getLimit(),
                                                c1.getActivity() + c2.getActivity(),
                                                c1.getAverageScore(),
                                                c1.getPopularity()
                                        )
                                ),
                                c -> (double) c.get().getPoints() / (double) c.get().getActivity()
                        )
                ));


        Optional<Map.Entry<Course.CourseName, Double>> minRatioEntry = ratioMap.entrySet().stream()
                .min(Map.Entry.comparingByValue());

        List<String> hardestCourses = minRatioEntry.map(e -> Collections.singletonList(e.getKey().getName()))
                .orElse(new ArrayList<>());


        Optional<Map.Entry<Course.CourseName, Double>> maxRatioEntry = ratioMap.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        List<String> easiestCourses = maxRatioEntry.map(e -> Collections.singletonList(e.getKey().getName()))
                .filter(entry -> !hardestCourses.contains(entry.get(0)))
                .orElse(new ArrayList<>());
        String hardestCoursesResult = hardestCourses.isEmpty() ? "n/a" : String.join(", ", hardestCourses);
        String easiestCoursesResult = easiestCourses.isEmpty() ? "n/a" : String.join(", ", easiestCourses);
        System.out.printf("Easiest course: %s\n", String.join(",", easiestCoursesResult));

        System.out.printf("Hardest course: %s\n", String.join(",", hardestCoursesResult));

    }
}
