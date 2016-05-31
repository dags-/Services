package me.dags.services.impl.warp.bedrock;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import me.dags.services.api.warp.Warp;

class BedrockWarp implements Warp {

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
}
