package me.dags.services.api.region.property;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.util.Tristate;

import me.dags.services.api.query.Property;
import me.dags.services.api.region.Region;

public interface UseItem extends Property<Region> {

    Tristate canUseItem(Player player, ItemType itemType);
}
