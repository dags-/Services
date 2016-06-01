package me.dags.services.integrations.dynmap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.dynmap.DynmapCommonAPI;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.world.World;

import me.dags.services.api.NamedService;
import me.dags.services.api.warp.Warp;
import me.dags.services.api.warp.WarpMultiService;
import me.dags.services.api.warp.WarpService;

public class DynmapWarps {

    private final WarpMultiService warpService = Sponge.getServiceManager().provideUnchecked(WarpMultiService.class);
    private final DynmapCommonAPI dynmap;

    DynmapWarps(DynmapCommonAPI commonApi) {
        this.dynmap = commonApi;
    }

    void refresh() {
        for (WarpService service : warpService.getAll()) {
            refreshService(service);
        }
    }

    void refreshService(WarpService service) {
        for (World world : Sponge.getServer().getWorlds()) {
            refreshWorld(service, world);
        }
    }

    void refreshWorld(WarpService service, World world) {
        MarkerSet markerSet = getMarkerSet(service);
        for (Warp warp : service.getWorldWarps(world)) {
            refresh(warp, markerSet);
        }
    }

    private void refresh(Warp warp, MarkerSet markerSet) {
        Marker marker = markerSet.findMarkerByLabel(warp.getName());
        try {
            MarkerIcon icon = warp.getMarker().map(this::getIcon).orElse(null);
            String label = warp.getName();
            String world = warp.getLocation().getExtent().getName();
            double x = warp.getLocation().getX();
            double y = warp.getLocation().getY();
            double z = warp.getLocation().getZ();
            boolean persistent = true;
            if (marker == null) {
                marker = markerSet.createMarker(null, label, world, x, y, z, icon, persistent);
            } else {
                marker.setLabel(label, persistent);
                marker.setLocation(world, x, y, z);
                marker.setMarkerIcon(icon);
            }
            applyMeta(warp, marker);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applyMeta(Warp warp, Marker marker) {
        StringBuilder builder = new StringBuilder();
        List<String> lines = warp.test(Warp.META, m -> m.htmlLines()).orElse(Collections.emptyList());
        Iterator<String> iterator = lines.iterator();
        while (iterator.hasNext()) {
            builder.append(iterator.next());
            if (iterator.hasNext()) {
                builder.append("<br>");
            }
        }
        marker.setDescription(builder.toString());
    }

    private String assetName(Asset asset) {
        String url = asset.getUrl().toString();
        int start = url.lastIndexOf('/') + 1, end = url.lastIndexOf('.');
        start = start > -1 ? start : 0;
        end = end > -1 ? end : url.length() - 1;
        return url.substring(start, end).toLowerCase();
    }

    private MarkerIcon getIcon(Asset asset) {
        String name = assetName(asset);
        String id = asset.getOwner().getId() + "_" + name;
        MarkerIcon icon = dynmap.getMarkerAPI().getMarkerIcon(id);
        if (icon != null) {
            return icon;
        }
        try (InputStream inputStream = asset.getUrl().openStream()) {
            return dynmap.getMarkerAPI().createMarkerIcon(id, name, inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private MarkerSet getMarkerSet(NamedService service) {
        String id = service.getIdentifier();
        String name = service.getDisplayName();
        MarkerSet markerSet = dynmap.getMarkerAPI().getMarkerSet(id);
        if (markerSet == null) {
            markerSet = dynmap.getMarkerAPI().createMarkerSet(id, name, null, false);
        }
        return markerSet;
    }
}
