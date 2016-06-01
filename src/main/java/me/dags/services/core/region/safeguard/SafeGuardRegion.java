package me.dags.services.core.region.safeguard;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.profile.GameProfile;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.util.Color;
import org.spongepowered.api.util.Tristate;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.helion3.safeguard.zones.Zone;

import me.dags.services.api.dynmap.property.Description;
import me.dags.services.api.dynmap.property.Shape.Rectangular;
import me.dags.services.api.dynmap.property.Style;
import me.dags.services.api.region.Region;
import me.dags.services.api.region.property.Build;
import me.dags.services.api.region.property.Damage;

class SafeGuardRegion implements Region, Build, Damage, Rectangular, Description, Style {

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

    @Override
    public String name() {
        return zone.getName();
    }

    @Override
    public Vector3d min() {
        return zone.getVolume().getMin().toDouble();
    }

    @Override
    public Vector3d max() {
        return zone.getVolume().getMax().toDouble();
    }

    @Override
    public List<String> htmlLines() {
        List<String> lines = new ArrayList<>();
        lines.add("<b>owners:</b>");
        for (GameProfile profile : zone.getOwners()) {
            if (profile.getName().isPresent()) {
                lines.add("<li>" + profile.getName().get() + "</li>");
            }
        }
        return lines;
    }

    @Override
    public int lineColor() {
        return Color.DARK_MAGENTA.getRgb();
    }

    @Override
    public int lineWeight() {
        return 2;
    }

    @Override
    public double lineOpacity() {
        return 0.5;
    }

    @Override
    public int fillColor() {
        return Color.LIME.getRgb();
    }

    @Override
    public double fillOpacity() {
        return 0.2;
    }
}
