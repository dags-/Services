package me.dags.services.api.warp;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import me.dags.services.api.query.Query;
import me.dags.services.api.query.Queryable;
import me.dags.services.api.warp.property.DisplayMeta;

public interface Warp extends Queryable<Warp> {

    Query<Warp, DisplayMeta> META = new Query<>(DisplayMeta.class);

    String getName();

    Location<World> getLocation();

    default Optional<Asset> getMarker() {
        return Sponge.getPluginManager().getPlugin("services").flatMap(p -> p.getAsset("dynmap/warp.png"));
    }
}