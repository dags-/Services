package me.dags.services.api.dynmap.property;

import java.util.List;

import me.dags.services.api.query.Property;

public interface Description extends Property {

    List<String> htmlLines();
}
