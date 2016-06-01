package me.dags.services.integrations.dynmap;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;

import me.dags.services.integrations.UpdatableIntegration;

public class DynmapMain extends DynmapCommonAPIListener implements UpdatableIntegration {

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
