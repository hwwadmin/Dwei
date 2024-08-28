package com.dwei.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pair<A, B> {

    private A first;
    private B second;

    public static <A, B> Pair<A, B> of() {
        return new Pair<>();
    }

    public static <A, B> Pair<A, B> of(A first, B second) {
        return new Pair<>(first, second);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final Pair<?, ?> other)) return false;
        if (!Objects.equals(this.getFirst(), other.getFirst())) return false;
        return Objects.equals(this.getSecond(), other.getSecond());
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $first = this.getFirst();
        result = result * PRIME + ($first == null ? 43 : $first.hashCode());
        final Object $second = this.getSecond();
        result = result * PRIME + ($second == null ? 43 : $second.hashCode());
        return result;
    }

}
