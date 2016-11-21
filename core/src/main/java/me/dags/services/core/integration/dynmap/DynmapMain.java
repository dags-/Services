package me.dags.services.core.integration.dynmap;

import me.dags.commandbus.CommandBus;
import me.dags.commandbus.annotation.Caller;
import me.dags.commandbus.annotation.Command;
import me.dags.commandbus.annotation.One;
import me.dags.commandbus.annotation.Permission;
import me.dags.commandbus.utils.Format;
import me.dags.data.node.Node;
import me.dags.data.node.NodeObject;
import me.dags.services.api.property.Query;
import me.dags.services.api.property.dynmap.Shape;
import me.dags.services.api.property.dynmap.ShapeStyle;
import me.dags.services.core.Services;
import me.dags.services.core.config.Config;
import me.dags.services.core.integration.UpdatableIntegration;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class DynmapMain extends DynmapCommonAPIListener implements UpdatableIntegration {

    static final Query<Shape> SHAPE = new Query<>(Shape.class);
    static final Query<Shape.Circular> CIRCULAR = new Query<>(Shape.Circular.class);
    static final Query<Shape.Polygonal> POLYGONAL = new Query<>(Shape.Polygonal.class);
    static final Query<Shape.Rectangular> RECTANGULAR = new Query<>(Shape.Rectangular.class);
    static final Query<ShapeStyle> SHAPE_STYLE = new Query<>(ShapeStyle.class);

    private DynmapWarps dynmapWarps = null;
    private DynmapRegions dynmapRegions = null;
    private Config config = null;

    @Override
    public void apiEnabled(DynmapCommonAPI commonApi) {
        this.dynmapWarps = new DynmapWarps(this, commonApi);
        this.dynmapRegions = new DynmapRegions(this, commonApi);
    }

    @Override
    public Collection<String> getIdentifiers() {
        return Arrays.asList("regions", "warps");
    }

    @Override
    public void init() {
        config = new Config("dynmap");
        CommandBus.create().register(this).submit(Services.getInstance());
        DynmapCommonAPIListener.register(this);
        Optional<?> optional = Sponge.getPluginManager().getPlugin("services").flatMap(PluginContainer::getInstance);
        optional.ifPresent(p -> Sponge.getScheduler().createTaskBuilder().execute((Runnable) this::update).submit(p));
    }

    @Override
    public void update(String identifier) {
        if (identifier.equals("regions") && dynmapRegions != null) {
            dynmapRegions.refresh();
        } else if (identifier.equals("warps") && dynmapWarps != null) {
            dynmapWarps.refresh();
        }
    }

    @Command(aliases = "map", parent = "dynworld", perm = @Permission("service.dynmap.commands"))
    public void setWorldMapping(@Caller CommandSource source, @One("world") String world, @One("mapping") String mapping) {
        Format.DEFAULT.info("Set world mapping ").stress(world).info("=").stress(mapping).tell(source);
        NodeObject mappings = config.getObject("world_mappings");
        mappings = mappings.isPresent() ? mappings : new NodeObject();
        mappings.put(world, mapping);
        config.set("world_mappings", mappings);
        update();
    }

    String getWorldMapping(String name) {
        NodeObject mappings = config.getObject("world_mappings");
        if (mappings.isPresent()) {
            Node mapping = mappings.get(name.toLowerCase());
            if (mapping.isPresent()) {
                return mapping.toString();
            }
        } else {
            mappings = new NodeObject();
        }
        mappings.put(name.toLowerCase(), name);
        config.set("world_mappings", mappings);
        return name;
    }
}
