package me.dags.services.api.property.action;

import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.util.Tristate;

import me.dags.services.api.property.Property;

public interface Traverse extends Property {

    Tristate canEnter(Subject subject);

    Tristate canLeave(Subject subject);

    Tristate canMove(Subject subject);
}
