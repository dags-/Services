package me.dags.services.api.property.action;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.util.Tristate;

import me.dags.services.api.property.Property;

public interface UseItem extends Property {

    Tristate canUseItem(Player player, ItemType itemType);
}
