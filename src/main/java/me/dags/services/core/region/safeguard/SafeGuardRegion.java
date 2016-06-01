package me.dags.services.core.region.safeguard;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.util.Tristate;

import com.flowpowered.math.vector.Vector3i;
import com.helion3.safeguard.zones.Zone;

import me.dags.services.api.region.Region;
import me.dags.services.api.region.property.Build;
import me.dags.services.api.region.property.Damage;

class SafeGuardRegion implements Region, Build, Damage {

    private final Zone zone;

    SafeGuardRegion(Zone zone) {
        this.zone = zone;
    }

    @Override
    public boolean contains(Vector3i position) {
        return zone.getVolume().contains(position);
    }

    @Override
    public Tristate canPlaceBlock(Subject subject, BlockSnapshot block) {
        if (subject.hasPermission("safeguard.mod")) {
            return Tristate.TRUE;
        }
        if (subject instanceof Player) {
            return Tristate.fromBoolean(zone.allows((Player) subject, "block.change"));
        }
        return Tristate.UNDEFINED;
    }

    @Override
    public Tristate canBreakBlock(Subject subject, BlockSnapshot block) {
        return canPlaceBlock(subject, block);
    }

    @Override
    public Tristate canDamageEntity(Entity source, Entity target) {
        if (source instanceof Player && target instanceof Player) {
            return Tristate.fromBoolean(zone.allows((Player) source, "damage.player"));
        }
        return Tristate.UNDEFINED;
    }
}
