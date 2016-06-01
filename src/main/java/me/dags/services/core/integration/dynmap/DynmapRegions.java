package me.dags.services.core.integration.dynmap;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.dynmap.DynmapCommonAPI;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.CircleMarker;
import org.dynmap.markers.GenericMarker;
import org.dynmap.markers.MarkerDescription;
import org.dynmap.markers.MarkerSet;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;

import me.dags.services.api.property.Query;
import me.dags.services.api.property.dynmap.Shape;
import me.dags.services.api.property.meta.Description;
import me.dags.services.api.region.Region;
import me.dags.services.api.region.RegionMultiService;
import me.dags.services.api.region.RegionService;

public class DynmapRegions {

    private final RegionMultiService regions = Sponge.getServiceManager().provideUnchecked(RegionMultiService.class);
    private final DynmapCommonAPI dynmap;

    DynmapRegions(DynmapCommonAPI commonApi) {
        this.dynmap = commonApi;
    }

    void refresh() {
        for (World world : Sponge.getServer().getWorlds()) {
            refreshWorld(world);
        }
    }

    void refreshWorld(World world) {
        for (RegionService service : regions.getAll()) {
            for (Region region : service.getRegions(world)) {
                refresh(service, region, world);
            }
        }
    }

    void refresh(RegionService service, Region region, World world) {
        region.accept(DynmapMain.SHAPE, r -> {
            MarkerSet markerSet = dynmap.getMarkerAPI().getMarkerSet(service.getIdentifier());
            markerSet = markerSet != null ? markerSet : dynmap.getMarkerAPI().createMarkerSet(service.getIdentifier(), service.getDisplayName(), null, false);
            markerSet.setHideByDefault(service.hideByDefault());
            refreshMarker(markerSet, region, r.displayName(), world.getName());
        });
    }

    private void refreshMarker(MarkerSet markerSet, Region region, String name, String world) {
        String id = (world + "_" + name).toLowerCase();

        markerSet.getAreaMarkers().stream().filter(m -> m.getMarkerID().equals(id)).forEach(GenericMarker ::deleteMarker);
        markerSet.getCircleMarkers().stream().filter(m -> m.getMarkerID().equals(id)).forEach(GenericMarker ::deleteMarker);

        if (region.supports(DynmapMain.CIRCULAR)) {
            circular(markerSet, region, id, name, world);
        } else if (region.supports(DynmapMain.POLYGONAL)) {
            polygonal(markerSet, region, id, name, world);
        } else if (region.supports(DynmapMain.RECTANGULAR)) {
            rectangular(markerSet, region, id, name, world);
        }
    }

    private void circular(MarkerSet markerSet, Region region, String id, String name, String world) {
        Shape.Circular circle = region.map(DynmapMain.CIRCULAR).get();
        boolean html = true;
        double x = circle.center().getX();
        double y = circle.center().getY();
        double z = circle.center().getZ();
        double rad = circle.radius();
        CircleMarker marker = markerSet.createCircleMarker(id, name, html, world, x, y, z, rad, rad, true);
        region.accept(Query.DESCRIPTION, description(marker));
        region.accept(DynmapMain.SHAPE_STYLE, s -> {
            marker.setLineStyle(s.lineWeight(), s.lineOpacity(), s.lineColor());
            marker.setFillStyle(s.fillOpacity(), s.fillColor());
        });
    }

    private void polygonal(MarkerSet markerSet, Region region, String id, String name, String world) {
        Shape.Polygonal polygon = region.map(DynmapMain.POLYGONAL).get();
        List<Vector3d> points = polygon.points();
        boolean html = true;
        double[] x = new double[points.size()];
        double[] z = new double[points.size()];
        for (int i = 0; i < points.size(); i++) {
            x[i] = points.get(i).getX();
            z[i] = points.get(i).getZ();
        }
        AreaMarker marker = markerSet.createAreaMarker(id, name, html, world, x, z, true);
        region.accept(Query.DESCRIPTION, description(marker));
        region.accept(DynmapMain.SHAPE_STYLE, s -> {
            marker.setLineStyle(s.lineWeight(), s.lineOpacity(), s.lineColor());
            marker.setFillStyle(s.fillOpacity(), s.fillColor());
        });
    }

    private void rectangular(MarkerSet markerSet, Region region, String id, String name, String world) {
        Shape.Rectangular polygon = region.map(DynmapMain.RECTANGULAR).get();
        boolean html = true;
        double[] x = new double[4];
        double[] z = new double[4];
        x[0] = polygon.min().getX();
        z[0] = polygon.min().getZ();
        x[1] = polygon.min().getX();
        z[1] = polygon.max().getZ();
        x[2] = polygon.max().getX();
        z[2] = polygon.max().getZ();
        x[3] = polygon.max().getX();
        z[3] = polygon.min().getZ();
        AreaMarker marker = markerSet.createAreaMarker(id, name, html, world, x, z, true);
        region.accept(Query.DESCRIPTION, description(marker));
        region.accept(DynmapMain.SHAPE_STYLE, s -> {
            marker.setLineStyle(s.lineWeight(), s.lineOpacity(), s.lineColor());
            marker.setFillStyle(s.fillOpacity(), s.fillColor());
        });
    }

    private Consumer<Description> description(MarkerDescription marker) {
        return d -> {
            StringBuilder builder = new StringBuilder();
            Iterator<String> iterator = d.lines().iterator();
            while (iterator.hasNext()) {
                builder.append(iterator.next());
                if (iterator.hasNext()) {
                    builder.append("<br>");
                }
            }
            marker.setDescription(builder.toString());
        };
    }
}
