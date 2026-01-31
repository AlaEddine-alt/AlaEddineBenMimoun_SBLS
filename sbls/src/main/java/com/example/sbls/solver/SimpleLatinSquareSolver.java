package com.example.sbls.solver;

import com.example.sbls.model.SolverResult;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.IntVar;

public class SimpleLatinSquareSolver {

    public static SolverResult solve(int n) {
        Model model = new Model("Simple Latin Square n=" + n);

        IntVar[][] grid = model.intVarMatrix("grid", n, n, 0, n - 1);

        // Row & column constraints
        for (int i = 0; i < n; i++) {
            model.allDifferent(grid[i]).post();

            IntVar[] col = new IntVar[n];
            for (int j = 0; j < n; j++) col[j] = grid[j][i];
            model.allDifferent(col).post();
        }

        Solver solver = model.getSolver();
        solver.setSearch(Search.minDomLBSearch(model.retrieveIntVars(true)));

        solver.limitTime("60s"); // safe max time

        long start = System.currentTimeMillis();
        boolean solved = solver.solve();
        long time = System.currentTimeMillis() - start;

        return new SolverResult(n, time, solver.getNodeCount(), solved);
    }
}
