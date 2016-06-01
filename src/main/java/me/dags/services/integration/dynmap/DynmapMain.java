package me.dags.services.integration.dynmap;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;
import org.spongepowered.api.Sponge;

import me.dags.services.api.dynmap.property.Description;
import me.dags.services.api.dynmap.property.Shape;
import me.dags.services.api.dynmap.property.ShapeStyle;
import me.dags.services.api.query.Query;
import me.dags.services.integration.UpdatableIntegration;

public class DynmapMain extends DynmapCommonAPIListener implements UpdatableIntegration {

    static final Query<Description> DESCRIPTION = new Query<>(Description.class);
    static final Query<Shape> SHAPE = new Query<>(Shape.class);
    static final Query<Shape.Circular> CIRCULAR = new Query<>(Shape.Circular.class);
    static final Query<Shape.Polygonal> POLYGONAL = new Query<>(Shape.Polygonal.class);
    static final Query<Shape.Rectangular> RECTANGULAR = new Query<>(Shape.Rectangular.class);
    static final Query<ShapeStyle> SHAPE_STYLE = new Query<>(ShapeStyle.class);

    private DynmapWarps dynmapWarps = null;
    private DynmapRegions dynmapRegions = null;

    @Override
    public void apiEnabled(DynmapCommonAPI commonApi) {
        this.dynmapWarps = new DynmapWarps(commonApi);
        this.dynmapRegions = new DynmapRegions(commonApi);
    }

    @Override
    public Collection<String> getIdentifiers() {
        return Arrays.asList("regions", "warps");
    }

    @Override
    public void init() {
        DynmapCommonAPIListener.register(this);
        Optional<?> optional = Sponge.getPluginManager().getPlugin("services").flatMap(c -> c.getInstance());
        optional.ifPresent(p -> Sponge.getScheduler().createTaskBuilder().execute(() -> update()).submit(p));
    }

    @Override
    public void update(String identifier) {
        if (identifier.equals("regions") && dynmapRegions != null) {
            dynmapRegions.refresh();
        } else if (identifier.equals("warps") && dynmapWarps != null) {
            dynmapWarps.refresh();
        }
    }
}
