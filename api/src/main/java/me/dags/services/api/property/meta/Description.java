package me.dags.services.api.property.meta;

import java.util.List;

import me.dags.services.api.property.Property;

public interface Description extends Property {

    List<String> lines();
}
