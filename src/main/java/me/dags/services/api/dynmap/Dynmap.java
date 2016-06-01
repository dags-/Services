package me.dags.services.api.dynmap;

import me.dags.services.api.dynmap.property.Description;
import me.dags.services.api.dynmap.property.Shape;
import me.dags.services.api.dynmap.property.Style;
import me.dags.services.api.query.Query;

public interface Dynmap {

    Query<Description> DESCRIPTION = new Query<>(Description.class);
    Query<Shape> SHAPE = new Query<>(Shape.class);
    Query<Shape.Circular> CIRCULAR = new Query<>(Shape.Circular.class);
    Query<Shape.Polygonal> POLYGONAL = new Query<>(Shape.Polygonal.class);
    Query<Shape.Rectangular> RECTANGULAR = new Query<>(Shape.Rectangular.class);
    Query<Style> STYLE = new Query<>(Style.class);
}
