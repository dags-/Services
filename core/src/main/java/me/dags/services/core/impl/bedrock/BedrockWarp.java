package me.dags.services.core.impl.bedrock;

import me.dags.services.api.property.meta.Description;
import me.dags.services.api.warp.Warp;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.List;

class BedrockWarp implements Warp, Description {

    private final String name;
    private final Location<World> location;

    BedrockWarp(String name, Location<World> location) {
        this.name = name;
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
        lines.add("<b>world</b>: " + location.getExtent().getName());
        lines.add("<b>x</b>: " + location.getBlockX());
        lines.add("<b>y</b>: " + location.getBlockY());
        lines.add("<b>z</b>: " + location.getBlockZ());
        return lines;
    }
}