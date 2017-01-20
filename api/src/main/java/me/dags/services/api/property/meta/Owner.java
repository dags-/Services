package me.dags.services.api.property.meta;

import me.dags.services.api.property.Property;

import java.util.Collection;
import java.util.Optional;

public interface Owner extends Property {

    Optional<String> owner();

    Optional<Collection<String>> owners();
}
