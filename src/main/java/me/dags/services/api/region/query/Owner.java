package me.dags.services.api.region.query;

import java.util.Collection;
import java.util.Optional;

import me.dags.services.api.query.Property;
import me.dags.services.api.region.Region;

public interface Owner extends Property<Region> {

    Optional<String> ownerIdentifier();

    Optional<Collection<String>> ownerIdentifiers();
}
