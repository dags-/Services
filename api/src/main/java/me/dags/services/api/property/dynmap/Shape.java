package me.dags.services.api.property.dynmap;

import java.util.List;

import com.flowpowered.math.vector.Vector3d;

import me.dags.services.api.property.Property;

public interface Shape extends Property {

    String displayName();

    interface Circular extends Shape {

        Vector3d center();

        int radius();
    }

    interface Rectangular extends Shape {

        Vector3d min();

        Vector3d max();
    }

    interface Polygonal extends Shape {

        List<Vector3d> points();
    }
}
