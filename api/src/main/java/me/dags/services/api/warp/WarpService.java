package me.dags.services.api.warp;

import java.util.Collection;
import java.util.Optional;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import me.dags.services.api.NamedService;
import me.dags.services.api.property.dynmap.Visibility;

public interface WarpService extends NamedService, Visibility {

    Optional<Warp> getWarp(String name);

    Collection<Warp> getWarps(World world);

    Collection<Warp> getNearbyWarps(Location<World> position, int radius);

    @Override
    default boolean hideByDefault() {
        return false;
    }
}
