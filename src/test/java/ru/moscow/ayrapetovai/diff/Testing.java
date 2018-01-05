package ru.moscow.ayrapetovai.diff;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.moscow.ayrapetovai.diff.algo.Instruction;
import ru.moscow.ayrapetovai.diff.algo.MergeAlgorithm;
import ru.moscow.ayrapetovai.diff.algo.DiffAlgorithm;

import java.util.Arrays;
import java.util.List;

public class Testing {
    @ParameterizedTest
    @CsvSource({
            "123, 123",
            "123, 13",
            "123, 0123",
            "123, 12345",
            "0123, 1234",
            "123, 1223",
            "1223, 1224223",
            "12121, 32121",
            "12121, 12321",
            "51234, 125634",
            "123, 321",
            "51235123512341235, 123612152512352135",
            "_A_B_C_D, A_BC_D_",
            "ABCABBA, CBABAC",
            "12345, 25143",
            "12123, 11322"
    })
    public void test(String as, String bs) {
        char[] a = as.toCharArray();
        char[] b = bs.toCharArray();

        DiffAlgorithm diff = new DiffAlgorithm();
        System.out.println(as + " -> " + bs);
        List<Instruction> instructions = diff.diff(a, b);
        System.out.println(instructions);

        MergeAlgorithm merge = new MergeAlgorithm();
        char[] r = merge.merge(instructions, a);

        System.out.println(Arrays.equals(b, r));
        System.out.println(new String(r));
        Assertions.assertArrayEquals(b, r);

        System.out.println("-----------------");
    }

//    @Test
//    public void testFindConflicts() {
//        char[] a = "12345".toCharArray();
//        char[] b = "12645".toCharArray();
//        char[] c = "123475".toCharArray();
//
//        DiffAlgorithm diff = new DiffAlgorithm();
//        List<Instruction> ab = diff.diff(a, b);
//
//        List<Instruction> ac = diff.diff(a, c);
//
//        CombinerAlgorithm cmb = new CombinerAlgorithm();
//        List<InstructionGroup> result = cmb.combine(ab, ac);
//
//        for (InstructionGroup g: result) {
//            if (!g.isConflict()) {
//
//            }
//        }
//    }
}
