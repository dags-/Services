package me.dags.services.api.query;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Queryable {

    default boolean supports(Query<?> query) {
        return query.type.isInstance(this);
    }

    default <T extends Property> Optional<T> map(Query<T> query) {
        if (supports(query)) {
            return Optional.of(query.type.cast(this));
        }
        return Optional.empty();
    }

    default <T extends Property> void accept(Query<T> query, Consumer<T> consumer) {
        if (supports(query)) {
            consumer.accept(query.type.cast(this));
        }
    }

    default <T extends Property, R> Optional<R> apply(Query<T> query, Function<T, R> function) {
        if (supports(query)) {
            return Optional.ofNullable(function.apply(query.type.cast(this)));
        }
        return Optional.empty();
    }
}
