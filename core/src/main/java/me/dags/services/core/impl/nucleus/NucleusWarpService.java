package me.dags.services.core.impl.nucleus;

import io.github.nucleuspowered.nucleus.api.data.WarpData;
import io.github.nucleuspowered.nucleus.modules.warp.handlers.WarpHandler;
import me.dags.services.api.warp.Warp;
import me.dags.services.api.warp.WarpService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author dags <dags@dags.me>
 */
public class NucleusWarpService implements WarpService {

    @Override
    public String getDisplayName() {
        return "Nucleus Warps";
    }

    @Override
    public String getIdentifier() {
        return "nucleus_warps";
    }

    @Override
    public Optional<? extends Warp> getWarp(String name) {
        return getWarpHandler().getWarp(name).map(NucleusWarp::new);
    }

    @Override
    public Collection<? extends Warp> getWarps(World world) {
        return getWorldWarps(world)
                .map(NucleusWarp::new)
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends Warp> getNearbyWarps(Location<World> position, int radius) {
        int radSq = radius * radius;
        return getWorldWarps(position.getExtent())
                .filter(warp -> warp.getLocation()
                        .map(l -> l.getBlockPosition().distanceSquared(position.getBlockPosition()) <= radSq)
                        .orElse(false))
                .map(NucleusWarp::new)
                .collect(Collectors.toList());
    }

    private Stream<WarpData> getWorldWarps(World world) {
        WarpHandler handler = getWarpHandler();
        return handler.getWarpNames().stream()
                .map(handler::getWarp).filter(Optional::isPresent).map(Optional::get)
                .filter(warp -> warp.getLocation().map(l -> l.getExtent().equals(world)).orElse(false));
    }

    private WarpHandler getWarpHandler() {
        return Sponge.getServiceManager().provideUnchecked(WarpHandler.class);
    }
}
