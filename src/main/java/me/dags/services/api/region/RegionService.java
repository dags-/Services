package me.dags.services.api.region;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import me.dags.services.api.NamedService;
import me.dags.services.api.query.Property;
import me.dags.services.api.query.Query;

public interface RegionService extends NamedService {

    Collection<Region> getRegions(World world);

    Stream<Region> getRegions(Location<World> location);

    default Stream<Region> getSortedRegions(Location<World> location) {
        return getRegions(location).sorted((r1, r2) -> r1.priority()> r2.priority() ? -1 : 1);
    }

    default <T extends Property<Region>> Stream<T> getRegions(Location<World> location, Query<Region, T> query) {
        return getRegions(location).map(r -> r.map(query)).filter(o -> o.isPresent()).map(o -> o.get());
    }

    // gets the region with the highest priority at the given location
    default Optional<Region> getTopRegion(Location<World> location) {
        return getRegions(location).max((r1, r2) -> r1.priority() > r2.priority() ? 1 : -1);
    }

    // gets the region with the highest priority at the given location as the given query type
    default <T extends Property<Region>> Optional<T> getTopQuery(Location<World> location, Query<Region, T> query) {
        return getSortedRegions(location).map(r -> r.map(query)).filter(Optional::isPresent).map(Optional::get).findFirst();
    }
}
