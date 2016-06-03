package me.dags.services.api.property.action;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.Tristate;

import me.dags.services.api.property.Property;

public interface Interact extends Property {

    Tristate canInteractBlock(Player player, BlockSnapshot block);

    Tristate canInteractLiving(Player player, Living target);

    Tristate canInteractEntity(Player player, Entity target);
}
