package me.dags.services.api.region.query;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.util.Tristate;

import me.dags.services.api.query.Property;
import me.dags.services.api.region.Region;

public interface Build extends Property<Region> {

    Tristate canPlaceBlock(Subject subject, BlockSnapshot block);

    Tristate canBreakBlock(Subject subject, BlockSnapshot block);
}
