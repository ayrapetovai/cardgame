package ru.moscow.ayrapetovai.diff.algo;

import java.util.ArrayList;

public class Copy implements Instruction {
    final int from;
    final int to;

    public Copy(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void apply(MergeContext mergeContext) {
        if (mergeContext.appended[to] == null) {
            mergeContext.appended[to] = new ArrayList<>();
        }
        mergeContext.appended[to].add(mergeContext.original[from]);
    }

    @Override
    public String toString() {
        return "copy from " + from + " to " + to;
    }
}
