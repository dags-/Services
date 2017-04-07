package me.dags.services.core.impl.nucleus;

import io.github.nucleuspowered.nucleus.api.service.NucleusWarpService;
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
public class NucleusWarpsService implements WarpService {

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
        return getNucleusWarps().getWarp(name).flatMap(this::toWarp);
    }

    @Override
    public Collection<? extends Warp> getWarps(World world) {
        return getWorldWarps(world)
                .map(this::toWarp)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends Warp> getNearbyWarps(Location<World> location, int radius) {
        int radSq = radius * radius;
        return getWarps(location.getExtent()).stream()
                .filter(w -> w.getLocation().getBlockPosition()
                        .distanceSquared(location.getBlockX(), location.getBlockY(), location.getBlockZ()) <= radSq)
                .collect(Collectors.toList());
    }

    private Optional<Warp> toWarp(io.github.nucleuspowered.nucleus.api.nucleusdata.Warp warp) {
        Location<World> location = warp.getLocation().orElse(null);
        String name = warp.getName();
        String category = warp.getCategory().orElse("");
        double cost = warp.getCost().orElse(-1D);
        return location == null ? Optional.empty() : Optional.of(new NucleusWarp(location, name, category, cost));
    }

    private Stream<io.github.nucleuspowered.nucleus.api.nucleusdata.Warp> getWorldWarps(World world) {
        return getNucleusWarps().getAllWarps().stream()
                .filter(w -> w.getLocation().map(l -> l.getExtent().equals(world)).orElse(false));
    }

    private NucleusWarpService getNucleusWarps() {
        return Sponge.getServiceManager().provideUnchecked(NucleusWarpService.class);
    }
}
