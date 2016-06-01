package me.dags.services.integration;

import java.util.Collection;

public interface UpdatableIntegration {

    Collection<String> getIdentifiers();

    void init();

    void update(String identifier);

    default void update() {
        getIdentifiers().forEach(this::update);
    }
}
