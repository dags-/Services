package me.dags.services.api.region.property;

import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.util.Tristate;

import me.dags.services.api.query.Property;

public interface Traverse extends Property {

    Tristate canEnter(Subject subject);

    Tristate canLeave(Subject subject);

    Tristate canMove(Subject subject);
}
