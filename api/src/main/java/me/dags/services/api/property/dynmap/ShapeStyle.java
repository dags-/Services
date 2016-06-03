package me.dags.services.api.property.dynmap;

import me.dags.services.api.property.Property;

public interface ShapeStyle extends Property {

    int lineColor();

    int lineWeight();

    double lineOpacity();

    int fillColor();

    double fillOpacity();
}
