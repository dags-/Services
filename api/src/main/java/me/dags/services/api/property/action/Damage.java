package me.dags.services.api.property.action;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.util.Tristate;

import me.dags.services.api.property.Property;

public interface Damage extends Property {

    Tristate canDamageEntity(Entity source, Entity target);
}
