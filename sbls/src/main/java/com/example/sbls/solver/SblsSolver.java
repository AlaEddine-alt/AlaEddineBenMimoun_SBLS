package com.example.sbls.solver;

import com.example.sbls.model.SolverResult;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.List;

public class SblsSolver {

    public static SolverResult solve(int n) {
        Model model = new Model("SBLS n=" + n);

        IntVar[][] grid = model.intVarMatrix("grid", n, n, 0, n - 1);

        // ----------------------------
        // 1 Latin square constraints
        // ----------------------------
        for (int i = 0; i < n; i++) {
            model.allDifferent(grid[i]).post();

            IntVar[] col = new IntVar[n];
            for (int j = 0; j < n; j++) col[j] = grid[j][i];
            model.allDifferent(col).post();
        }

        // ----------------------------
        // 2 Symmetry breaking
        // ----------------------------
        for (int j = 0; j < n; j++) {
            model.arithm(grid[0][j], "=", j).post();
        }

        // ----------------------------
        // 3 SBLS distance sum constraints
        // ----------------------------
        // Compute sum of distances between all occurrences of each pair of colors
        List<IntVar> pairSums = new ArrayList<>();

        for (int c1 = 0; c1 < n; c1++) {
            for (int c2 = c1 + 1; c2 < n; c2++) {
                List<IntVar> distances = new ArrayList<>();

                for (int i1 = 0; i1 < n; i1++) {
                    for (int j1 = 0; j1 < n; j1++) {
                        for (int i2 = 0; i2 < n; i2++) {
                            for (int j2 = 0; j2 < n; j2++) {
                                // Only pairs with correct colors
                                IntVar isC1 = model.intVar(0,1);
                                model.ifThenElse(
                                        model.arithm(grid[i1][j1], "=", c1),
                                        model.arithm(isC1, "=", 1),
                                        model.arithm(isC1, "=", 0)
                                );

                                IntVar isC2 = model.intVar(0,1);
                                model.ifThenElse(
                                        model.arithm(grid[i2][j2], "=", c2),
                                        model.arithm(isC2, "=", 1),
                                        model.arithm(isC2, "=", 0)
                                );

                                // Manhattan distance * both flags
                                IntVar dist = model.intVar(0, 2*n);
                                model.arithm(dist, "=", Math.abs(i1 - i2) + Math.abs(j1 - j2)).post();
                                distances.add(dist);
                            }
                        }
                    }
                }

                IntVar sumDist = model.intVar(0, n*n*2);
                model.sum(distances.toArray(new IntVar[0]), "=", sumDist).post();
                pairSums.add(sumDist);
            }
        }

        // Enforce all sums equal
        for (int k = 1; k < pairSums.size(); k++) {
            model.arithm(pairSums.get(k), "=", pairSums.get(0)).post();
        }

        // ----------------------------
        // 4 Solver
        // ----------------------------
        Solver solver = model.getSolver();
        solver.setSearch(Search.minDomLBSearch(model.retrieveIntVars(true)));
        solver.limitTime("60s"); // safe limit

        long start = System.currentTimeMillis();
        boolean solved = solver.solve();
        long time = System.currentTimeMillis() - start;

        return new SolverResult(n, time, solver.getNodeCount(), solved);
    }
}
