package ru.moscow.ayrapetovai.diff;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * Created by artem on 05.02.17.
 */
public class Dessision {
    private List<BooleanSupplier> conditions = new ArrayList<>();

    public Dessision condition(BooleanSupplier cond) {
        conditions.add(cond);
        return this;
    }

    public boolean isTrue() {
        for (BooleanSupplier cond: conditions) {
            boolean b = cond.getAsBoolean();
            if (b)
                return true;
        }
        return false;
    }
}
