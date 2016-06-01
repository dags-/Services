package me.dags.services.impl.region.safeguard;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.helion3.safeguard.SafeGuard;

import me.dags.services.api.region.Region;
import me.dags.services.api.region.RegionService;

public class SafeGuardRegionService implements RegionService {

    @Override
    public String getDisplayName() {
        return "SafeGuard Regions";
    }

    @Override
    public String getIdentifier() {
        return "safeguard_regions";
    }

    @Override
    public Collection<Region> getRegions(World world) {
        return SafeGuard.getZoneManager().getZones().stream().map(SafeGuardRegion::new).collect(Collectors.toList());
    }

    @Override
    public Stream<Region> getRegions(Location<World> location) {
        return SafeGuard.getZoneManager().getZones(location).stream().map(SafeGuardRegion::new);
    }
}
