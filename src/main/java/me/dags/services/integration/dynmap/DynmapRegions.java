package me.dags.services.integration.dynmap;

import org.dynmap.DynmapCommonAPI;
import org.spongepowered.api.Sponge;

import me.dags.services.api.region.RegionMultiService;

public class DynmapRegions {

    private final RegionMultiService regions = Sponge.getServiceManager().provideUnchecked(RegionMultiService.class);
    private final DynmapCommonAPI dynmap;

    DynmapRegions(DynmapCommonAPI commonApi) {
        this.dynmap = commonApi;
    }

    void refresh() {

    }
}
