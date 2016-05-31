package me.dags.services.api.region;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3i;

import me.dags.services.api.query.Query;
import me.dags.services.api.query.Queryable;
import me.dags.services.api.region.query.Build;
import me.dags.services.api.region.query.Damage;
import me.dags.services.api.region.query.Interact;
import me.dags.services.api.region.query.Owner;
import me.dags.services.api.region.query.Traverse;
import me.dags.services.api.region.query.UseItem;

public interface Region extends Queryable<Region> {

    Query<Region, Build> BUILD = new Query<>(Build.class);
    Query<Region, Damage> DAMAGE = new Query<>(Damage.class);
    Query<Region, Interact> INTERACT = new Query<>(Interact.class);
    Query<Region, Owner> OWNER = new Query<>(Owner.class);
    Query<Region, Traverse> TRAVERSE = new Query<>(Traverse.class);
    Query<Region, UseItem> USE_ITEM = new Query<>(UseItem.class);

    boolean contains(Vector3i position);

    default boolean contains(Location<World> location) {
        return contains(location.getBlockPosition());
    }

    default int priority() {
        return -1;
    }
}
