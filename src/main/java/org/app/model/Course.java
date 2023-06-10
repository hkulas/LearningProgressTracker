package org.app.model;

public class Course {


    public enum CourseName {
        JAVA("Java"),
        DATA_STRUCTURES_AND_ALGORITHMS("DSA"),
        DATABASES("Databases"),
        SPRING("Spring");

        private final String name;

        CourseName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }


    private CourseName name;
    private int points;
    private int limit;
    private int activity;

    private double averageScore;

    private int popularity;

    private boolean isCompleted;


    public Course() {
    }

    public Course(CourseName name, int points, int limit, int activity, double averageScore, int popularity) {
        this.name = name;
        this.points = points;
        this.limit = limit;
        this.activity = activity;
        this.averageScore = averageScore;
        this.popularity = popularity;
        isCompleted = false;
    }

    public CourseName getName() {
        return name;
    }

    public void setName(CourseName name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
