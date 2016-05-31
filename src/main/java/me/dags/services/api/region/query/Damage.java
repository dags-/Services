package me.dags.services.api.region.query;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.util.Tristate;

import me.dags.services.api.query.Property;
import me.dags.services.api.region.Region;

public interface Damage extends Property<Region> {

    Tristate canDamageEntity(Entity source, Entity target);
}
