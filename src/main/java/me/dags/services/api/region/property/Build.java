package me.dags.services.api.region.property;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.util.Tristate;

import me.dags.services.api.query.Property;

public interface Build extends Property {

    Tristate canPlaceBlock(Subject subject, BlockSnapshot block);

    Tristate canBreakBlock(Subject subject, BlockSnapshot block);
}
