package me.dags.services.api.region;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3i;

import me.dags.services.api.query.Query;
import me.dags.services.api.query.Queryable;
import me.dags.services.api.region.property.Build;
import me.dags.services.api.region.property.Damage;
import me.dags.services.api.region.property.Interact;
import me.dags.services.api.region.property.Owner;
import me.dags.services.api.region.property.Traverse;
import me.dags.services.api.region.property.UseItem;

public interface Region extends Queryable {

    Query<Build> BUILD = new Query<>(Build.class);
    Query<Damage> DAMAGE = new Query<>(Damage.class);
    Query<Interact> INTERACT = new Query<>(Interact.class);
    Query<Owner> OWNER = new Query<>(Owner.class);
    Query<Traverse> TRAVERSE = new Query<>(Traverse.class);
    Query<UseItem> USE_ITEM = new Query<>(UseItem.class);

    boolean contains(Vector3i position);

    default boolean contains(Location<World> location) {
        return contains(location.getBlockPosition());
    }

    default int priority() {
        return -1;
    }
}
