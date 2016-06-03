package me.dags.services.core.impl.bedrock;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3i;
import com.helion3.bedrock.Bedrock;

import me.dags.services.api.warp.Warp;
import me.dags.services.api.warp.WarpService;

public class BedrockWarpService implements WarpService {

    @Override
    public String getDisplayName() {
        return "Bedrock Warps";
    }

    @Override
    public String getIdentifier() {
        return "bedrock_warps";
    }

    @Override
    public Optional<Warp> getWarp(String name) {
        Optional<Location<World>> optional = Bedrock.getWarpManager().getWarp(name);
        if (optional.isPresent()) {
            return Optional.of(new BedrockWarp(name, optional.get()));
        }
        return Optional.empty();
    }

    @Override
    public Collection<Warp> getWarps(World world) {
        return Bedrock.getWarpManager().getWarps().entrySet()
                .stream()
                .filter(e -> e.getValue().isPresent() && e.getValue().get().getExtent().equals(world))
                .map(e -> new BedrockWarp(e.getKey(), e.getValue().get()))
                .collect(Collectors.toList());

    }

    @Override
    public Collection<Warp> getNearbyWarps(Location<World> position, int radius) {
        final int squared = radius * radius;
        final Vector3i pos = position.getBlockPosition();
        return Bedrock.getWarpManager().getWarps().entrySet()
                .stream()
                .filter(e -> e.getValue().isPresent() && e.getValue().get().getBlockPosition().distanceSquared(pos) <= squared)
                .map(e -> new BedrockWarp(e.getKey(), e.getValue().get()))
                .collect(Collectors.toList());
    }
}
