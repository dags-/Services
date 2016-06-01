package me.dags.services.core.warp.bedrock;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import me.dags.services.api.dynmap.property.Description;
import me.dags.services.api.warp.Warp;

class BedrockWarp implements Warp, Description {

    private final String name;
    private final Location<World> location;
    private final List<String> lines = new ArrayList<>();

    BedrockWarp(String name, Location<World> location) {
        this.name = name;
        this.location = location;
        this.lines.add("<b>world</b>: " + location.getExtent().getName());
        this.lines.add("<b>x</b>: " + location.getBlockX());
        this.lines.add("<b>y</b>: " + location.getBlockY());
        this.lines.add("<b>z</b>: " + location.getBlockZ());
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
    public List<String> htmlLines() {
        return lines;
    }
}
