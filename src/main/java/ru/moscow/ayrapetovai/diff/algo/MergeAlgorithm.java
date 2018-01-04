package ru.moscow.ayrapetovai.diff.algo;

import java.util.List;

public class MergeAlgorithm {

    public char[] merge(List<Instruction> instructions, char[] original) {
        MergeContext mergeContext = new MergeContext(original);
        for (Instruction instruction: instructions) {
            if (instruction != null)
                instruction.apply(mergeContext);
        }
        return mergeContext.result();
    }
}
