package ru.moscow.ayrapetovai.diff;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.moscow.ayrapetovai.diff.ru.moscow.ayrapetovai.diff.DiffAlgorithm;

public class DiffTest {

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
            "51235123512341235, 123612152512352135"
    })
    public void test(String as, String bs) {
        char[] a = new char[as.length()];
        as.getChars(0, as.length(), a, 0);

        char[] b = new char[bs.length()];
        bs.getChars(0, bs.length(), b, 0);

        DiffAlgorithm diff = new DiffAlgorithm();
        diff.diff(a, b);

    }
}
