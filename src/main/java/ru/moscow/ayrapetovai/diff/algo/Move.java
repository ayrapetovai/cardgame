package ru.moscow.ayrapetovai.diff.algo;

import java.util.ArrayList;

public class Move implements Instruction {
    final int from;
    final int to;

    public Move(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void apply(MergeContext mergeContext) {
        mergeContext.deleted[from] = true;
        if (mergeContext.appended[to] == null) {
            mergeContext.appended[to] = new ArrayList<>();
        }
        mergeContext.appended[to].add(mergeContext.original[from]);
    }

    @Override
    public String toString() {
        return "move from " + from + " to " + to;
    }
}
