package ru.moscow.ayrapetovai.diff;

/**
 * Created by artem on 04.02.17.
 */
public class Difference {
    private final String base;
    private final String changed;

    public Difference(String base, String changed) {
        this.base = base;
        this.changed = changed;
    }

    public String getBase() {
        return base;
    }

    public String getChanged() {
        return changed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Difference that = (Difference) o;

        if (!base.equals(that.base)) return false;
        return changed.equals(that.changed);
    }

    @Override
    public int hashCode() {
        int result = base.hashCode();
        result = 31 * result + changed.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Difference{" +
                "base='" + base + '\'' +
                ", changed='" + changed + '\'' +
                '}';
    }
}
