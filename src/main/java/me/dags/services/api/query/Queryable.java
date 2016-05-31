package me.dags.services.api.query;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Queryable<T> {

    default boolean supports(Query<T, ?> query) {
        return query.type.isInstance(this);
    }

    default <V extends Property<T>> Optional<V> map(Query<T, V> query) {
        if (supports(query)) {
            return Optional.of(query.type.cast(this));
        }
        return Optional.empty();
    }

    default <V extends Property<T>> void accept(Query<T, V> query, Consumer<V> consumer) {
        if (supports(query)) {
            consumer.accept(query.type.cast(this));
        }
    }

    default <V extends Property<T>, R> Optional<R> test(Query<T, V> query, Function<V, R> function) {
        if (supports(query)) {
            return Optional.ofNullable(function.apply(query.type.cast(this)));
        }
        return Optional.empty();
    }
}
