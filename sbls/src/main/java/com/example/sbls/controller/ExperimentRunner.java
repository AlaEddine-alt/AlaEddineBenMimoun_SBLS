package com.example.sbls.controller;

import com.example.sbls.service.ExperimentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ExperimentRunner implements CommandLineRunner {

    private final ExperimentService experimentService;

    public ExperimentRunner(ExperimentService experimentService) {
        this.experimentService = experimentService;
    }

    @Override
    public void run(String... args) {
        experimentService.runExperiments(10);
    }
}
