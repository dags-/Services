package me.dags.services.api.region.property;

import java.util.Collection;
import java.util.Optional;

import me.dags.services.api.query.Property;

public interface Owner extends Property {

    Optional<String> owner();

    Optional<Collection<String>> owners();
}
