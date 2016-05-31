package me.dags.services.api.warp;

import java.util.Collection;
import java.util.Optional;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import me.dags.services.api.NamedService;

public interface WarpService extends NamedService {

    Optional<Warp> getWarp(String name);

    Collection<Warp> getWorldWarps(World world);

    Collection<Warp> getNearbyWarps(Location<World> position, int radius);
}
