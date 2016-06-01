package me.dags.services.api.region.property;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.util.Tristate;

import me.dags.services.api.query.Property;

public interface Damage extends Property {

    Tristate canDamageEntity(Entity source, Entity target);
}
