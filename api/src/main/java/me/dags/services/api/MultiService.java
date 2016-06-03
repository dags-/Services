package me.dags.services.api;

import java.util.Collection;
import java.util.Optional;

public interface MultiService<T extends NamedService> {

    Collection<T> getAll();

    Optional<T> getService(String name);

    boolean hasService(String name);

    void register(T service);
}
