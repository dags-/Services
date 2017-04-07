package me.dags.services.core.impl.nucleus;

import me.dags.services.api.property.meta.Description;
import me.dags.services.api.warp.Warp;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dags <dags@dags.me>
 */
public class NucleusWarp implements Warp, Description, Comparable<NucleusWarp> {

    private final double cost;
    private final String name;
    private final String category;
    private final Location<World> location;

    NucleusWarp(Location<World> location, String name, String category, double cost) {
        this.cost = cost;
        this.name = name;
        this.category = category;
        this.location = location;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Location<World> getLocation() {
        return location;
    }

    @Override
    public List<String> lines() {
        List<String> lines = new ArrayList<>();

        if (!category.isEmpty()) {
            lines.add("<b>category</b>: " + category);
        }

        if (cost > -1) {
            lines.add("<b>cost</b>: " + cost);
        }

        lines.add("<b>world</b>: " + location.getExtent().getName());
        lines.add("<b>x</b>: " + location.getBlockX());
        lines.add("<b>y</b>: " + location.getBlockY());
        lines.add("<b>z</b>: " + location.getBlockZ());

        return lines;
    }

    @Override
    public int compareTo(NucleusWarp other) {
        return this.getName().toLowerCase().compareTo(other.getName().toLowerCase());
    }
}
