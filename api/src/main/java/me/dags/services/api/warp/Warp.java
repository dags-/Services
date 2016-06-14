package me.dags.services.api.warp;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import me.dags.services.api.property.Queryable;

public interface Warp extends Queryable {

    String getName();

    Location<World> getLocation();

    default Optional<Asset> getMarker() {
        Optional<PluginContainer> plugin = Sponge.getPluginManager().getPlugin("services");
        if (plugin.isPresent()) {
            return plugin.flatMap(p -> p.getAsset("warp.png"));
        }
        return Optional.empty();
    }
}