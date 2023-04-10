package org.exampleM.utils;

import java.util.Objects;

public class Pair<T, U> {
    public final T first;
    public final U second;

    public Pair(T t, U u) {
        this.first = t;
        this.second = u;
    }

    /**
     * Comparison
     * @param o-pair
     * @return true if (a,b) = (x,y), else false
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
