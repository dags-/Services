package me.dags.services.api.dynmap.property;

import me.dags.services.api.query.Property;

public interface ShapeStyle extends Property {

    int lineColor();

    int lineWeight();

    double lineOpacity();

    int fillColor();

    double fillOpacity();
}
