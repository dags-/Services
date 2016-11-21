package me.dags.services.core.integration.dynmap;

import me.dags.services.api.NamedService;
import me.dags.services.api.property.Query;
import me.dags.services.api.warp.Warp;
import me.dags.services.api.warp.WarpMultiService;
import me.dags.services.api.warp.WarpService;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.world.World;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class DynmapWarps {

    private final WarpMultiService warpService = Sponge.getServiceManager().provideUnchecked(WarpMultiService.class);
    private final DynmapMain main;
    private final DynmapCommonAPI dynmap;

    DynmapWarps(DynmapMain main, DynmapCommonAPI commonApi) {
        this.main = main;
        this.dynmap = commonApi;
    }

    void refresh() {
        warpService.getAll().forEach(this::refreshService);
    }

    void refreshService(WarpService service) {
        dynmap.getMarkerAPI().getMarkerSets().stream()
                .filter(m -> m.getMarkerSetID().startsWith(service.getIdentifier()))
                .forEach(MarkerSet::deleteMarkerSet);

        for (World world : Sponge.getServer().getWorlds()) {
            refreshWorld(service, world);
        }
    }

    void refreshWorld(WarpService service, World world) {
        MarkerSet markerSet = getMarkerSet(service);
        markerSet.setHideByDefault(service.hideByDefault());
        for (Warp warp : service.getWarps(world)) {
            refresh(warp, markerSet);
        }
    }

    private void refresh(Warp warp, MarkerSet markerSet) {
        MarkerIcon icon = warp.getMarker().map(this::getIcon).orElse(null);
        String label = warp.getName();
        String world = main.getWorldMapping(warp.getLocation().getExtent().getName());
        String id = (world + "_" + label).toLowerCase();
        double x = warp.getLocation().getX();
        double y = warp.getLocation().getY();
        double z = warp.getLocation().getZ();

        Marker existing = markerSet.findMarker(id);
        Marker marker = existing != null ? existing : markerSet.createMarker(id, label, world, x, y, z, icon, true);
        marker.setMarkerIcon(icon);
        marker.setLabel(label, true);
        marker.setLocation(world, x, y, z);

        warp.accept(Query.DESCRIPTION, m -> {
            StringBuilder builder = new StringBuilder();
            Iterator<String> iterator = m.lines().iterator();
            while (iterator.hasNext()) {
                builder.append(iterator.next());
                if (iterator.hasNext()) {
                    builder.append("<br>");
                }
            }
            marker.setDescription(builder.toString());
        });
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
