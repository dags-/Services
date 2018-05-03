package me.dags.services.core.impl.bedrock;

import com.flowpowered.math.vector.Vector3i;
import com.helion3.bedrock.Bedrock;
import me.dags.services.api.warp.WarpService;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public Optional<BedrockWarp> getWarp(String name) {
        return Bedrock.getWarpManager().getWarp(name).map(location -> new BedrockWarp(name, location));
    }

    @Override
    public Collection<BedrockWarp> getWarps(World world) {
        return warps(world).collect(Collectors.toList());

    }

    @Override
    public Collection<BedrockWarp> getNearbyWarps(Location<World> position, int radius) {
        final int squared = radius * radius;
        final Vector3i pos = position.getBlockPosition();
        return warps(position.getExtent())
                .filter(w -> w.getLocation().getBlockPosition().distanceSquared(pos) <= squared)
                .collect(Collectors.toList());
    }

    private Stream<BedrockWarp> warps(World world) {
        return Bedrock.getWarpManager().getWarps().entrySet()
                .stream()
                .filter(e -> filterWarp(e, world))
                .map(e -> new BedrockWarp(e.getKey(), e.getValue().get()));
    }

    private boolean filterWarp(Map.Entry<String, Optional<Location<World>>> warp, World world) {
        Optional<Location<World>> location = warp.getValue();
        return location.isPresent() && location.get().getExtent().getName().equalsIgnoreCase(world.getName());
    }
}
