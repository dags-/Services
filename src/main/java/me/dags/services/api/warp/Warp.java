package me.dags.services.api.warp;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public interface Warp {

    String getName();

    Location<World> getLocation();
}
