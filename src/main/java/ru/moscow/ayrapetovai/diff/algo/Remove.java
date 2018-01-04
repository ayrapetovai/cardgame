package ru.moscow.ayrapetovai.diff.algo;

public class Remove implements Instruction {
    final int witch;

    public Remove(int witch) {
        this.witch = witch;
    }

    @Override
    public void apply(MergeContext mergeContext) {
        mergeContext.deleted[witch] = true;
    }
    @Override
    public String toString() {
        return "remove " + witch;
    }
}
