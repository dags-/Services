package me.dags.services.api.property.meta;

import me.dags.services.api.property.Property;

import java.util.List;

public interface Description extends Property {

    List<String> lines();
}
