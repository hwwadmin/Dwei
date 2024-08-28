package com.dwei.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Triple<A, B, C> {

    private A first;
    private B second;
    private C third;

    public static <A, B, C> Triple<A, B, C> of(A first, B second) {
        return new Triple<>(first, second, null);
    }

    public static <A, B, C> Triple<A, B, C> of(A first, B second, C third) {
        return new Triple<>(first, second, third);
    }

    public static <A, B, C> Triple<A, B, C> of(Pair<A, B> pair, C third) {
        return new Triple<>(pair.getFirst(), pair.getSecond(), third);
    }

    public static <A, B, C> Triple<A, B, C> of(A first, Pair<B, C> pair) {
        return new Triple<>(first, pair.getFirst(), pair.getSecond());
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof final Triple<?, ?, ?> other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$first = this.getFirst();
        final Object other$first = other.getFirst();
        if (!Objects.equals(this$first, other$first)) return false;
        final Object this$second = this.getSecond();
        final Object other$second = other.getSecond();
        if (!Objects.equals(this$second, other$second)) return false;
        final Object this$third = this.getThird();
        final Object other$third = other.getThird();
        return Objects.equals(this$third, other$third);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Triple;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $first = this.getFirst();
        result = result * PRIME + ($first == null ? 43 : $first.hashCode());
        final Object $second = this.getSecond();
        result = result * PRIME + ($second == null ? 43 : $second.hashCode());
        final Object $third = this.getThird();
        result = result * PRIME + ($third == null ? 43 : $third.hashCode());
        return result;
    }

}
