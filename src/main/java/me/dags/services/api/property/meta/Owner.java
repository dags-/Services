package me.dags.services.api.property.meta;

import java.util.Collection;
import java.util.Optional;

import me.dags.services.api.property.Property;

public interface Owner extends Property {

    Optional<String> owner();

    Optional<Collection<String>> owners();
}
