package me.dags.services.api.warp;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import me.dags.services.api.AbstractMultiService;

public class WarpMultiService extends AbstractMultiService<WarpService> implements WarpService {

    @Override
    public String getDisplayName() {
        return "Warp Service";
    }

    @Override
    public String getIdentifier() {
        return "warp_service";
    }

    @Override
    public Optional<Warp> getWarp(String name) {
        return services.values()
                .stream()
                .map(s -> s.getWarp(name))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    @Override
    public Collection<Warp> getWorldWarps(World world) {
        return services.values()
                .stream()
                .flatMap(s -> s.getWorldWarps(world).stream())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Warp> getNearbyWarps(Location<World> position, int radius) {
        return services.values()
                .stream()
                .flatMap(s -> s.getNearbyWarps(position, radius).stream())
                .collect(Collectors.toList());

    }
}
