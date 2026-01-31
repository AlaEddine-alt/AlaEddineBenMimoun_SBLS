package com.example.sbls.model;

public class SolverResult {

    private final int n;
    private final long timeMs;
    private final long nodes;
    private final boolean solved;

    public SolverResult(int n, long timeMs, long nodes, boolean solved) {
        this.n = n;
        this.timeMs = timeMs;
        this.nodes = nodes;
        this.solved = solved;
    }

    @Override
    public String toString() {
        return "n=" + n +
                " | time=" + timeMs + " ms" +
                " | nodes=" + nodes +
                " | solved=" + solved;
    }
}
