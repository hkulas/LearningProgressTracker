package org.app;

import org.app.command.CommandLineProcessor;
import org.app.service.NotificationService;
import org.app.service.StatisticsService;
import org.app.service.StudentService;
import org.app.validator.Validator;

public class Main {
    public static void main(String[] args) {

        System.out.println("Learning Progress Tracker");
        CommandLineProcessor commandLineProcessor = new CommandLineProcessor(
                new StudentService(
                        new Validator()),
                new StatisticsService(),
                new NotificationService());
        commandLineProcessor.processInput();

    }
}