package com.example.sbls.service;

import com.example.sbls.solver.SimpleLatinSquareSolver;
import com.example.sbls.solver.SblsSolver;
import org.springframework.stereotype.Service;

@Service
public class ExperimentService {

    public void runExperiments(int maxN) {
        System.out.println("Machine: Ryzen 5 5600H, 16GB RAM");
        for (int n = 2; n <= maxN; n++) {
            System.out.println("---- n = " + n + " ----");
            System.out.println("Simple Latin Square : " + SimpleLatinSquareSolver.solve(n));
            System.out.println("SBLS                 : " + SblsSolver.solve(n));
            System.out.println();
        }
    }
}
