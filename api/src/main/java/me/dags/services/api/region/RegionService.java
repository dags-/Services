package me.dags.services.api.region;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import me.dags.services.api.NamedService;
import me.dags.services.api.property.Property;
import me.dags.services.api.property.Query;
import me.dags.services.api.property.dynmap.Visibility;

public interface RegionService extends NamedService, Visibility {

    Collection<Region> getRegions(World world);

    Stream<Region> getRegions(Location<World> location);

    default Stream<Region> getSortedRegions(Location<World> location) {
        return getRegions(location).sorted((r1, r2) -> r1.priority()> r2.priority() ? -1 : 1);
    }

    default <T extends Property> Stream<T> getRegions(Location<World> location, Query<T> query) {
        return getRegions(location).map(r -> r.map(query)).filter(o -> o.isPresent()).map(o -> o.get());
    }

    default Optional<Region> getTopRegion(Location<World> location) {
        return getRegions(location).max((r1, r2) -> r1.priority() > r2.priority() ? 1 : -1);
    }

    default <T extends Property> Optional<T> getTopQuery(Location<World> location, Query<T> query) {
        return getSortedRegions(location).map(r -> r.map(query)).filter(Optional::isPresent).map(Optional::get).findFirst();
    }
}
