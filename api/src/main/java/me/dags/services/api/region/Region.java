package me.dags.services.api.region;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3i;

import me.dags.services.api.property.Queryable;

public interface Region extends Queryable {

    boolean contains(Vector3i position);

    default boolean contains(Location<World> location) {
        return contains(location.getBlockPosition());
    }

    default int priority() {
        return -1;
    }
}
