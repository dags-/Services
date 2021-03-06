package me.dags.services.api.region;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import me.dags.services.api.AbstractMultiService;

public final class RegionMultiService extends AbstractMultiService<RegionService> implements RegionService {

    @Override
    public String getDisplayName() {
        return "Region Service";
    }

    @Override
    public String getIdentifier() {
        return "region_service";
    }

    @Override
    public Collection<? extends Region> getRegions(World world) {
        return services.values().stream().flatMap(s -> s.getRegions(world).stream()).collect(Collectors.toList());
    }

    @Override
    public Stream<? extends Region> getRegions(Location<World> location) {
        return services.values().stream().flatMap(s -> s.getRegions(location));
    }
}