package me.dags.services.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.ServiceManager;
import org.spongepowered.api.text.Text;

import me.dags.commandbus.CommandBus;
import me.dags.commandbus.annotation.Caller;
import me.dags.commandbus.annotation.Command;
import me.dags.services.api.MultiService;
import me.dags.services.api.NamedService;
import me.dags.services.api.region.RegionMultiService;
import me.dags.services.api.region.RegionService;
import me.dags.services.api.warp.WarpMultiService;
import me.dags.services.api.warp.WarpService;
import me.dags.services.core.region.safeguard.SafeGuardRegionService;
import me.dags.services.core.warp.bedrock.BedrockWarpService;
import me.dags.services.integration.Integration;

@Plugin(name = "Services", id = "services", version = "1.0")
public class Services {

    private static final Logger logger = LoggerFactory.getLogger("Services");
    private static final RegionMultiService regionService = new RegionMultiService();
    private static final WarpMultiService warpService = new WarpMultiService();

    private final Integration dynmap = new Integration("org.dynmap.DynmapCommonAPI", "me.dags.services.integration.dynmap.DynmapMain");

    @Listener (order = Order.FIRST)
    public void preInit(GamePreInitializationEvent event) {
        CommandBus.newInstance(logger).register(this).submit(this);
        ServiceManager manager = Sponge.getServiceManager();
        manager.setProvider(this, RegionMultiService.class, regionService);
        manager.setProvider(this, RegionService.class, regionService);
        manager.setProvider(this, WarpMultiService.class, warpService);
        manager.setProvider(this, WarpService.class, warpService);
    }

    @Listener
    public void init(GameInitializationEvent event) {
        register("bedrock", warpService, BedrockWarpService.class);
        register("safeguard", regionService, SafeGuardRegionService.class);
        dynmap.init();
    }

    @Command(aliases = "refresh", parent = "service")
    public void refresh(@Caller CommandSource source) {
        source.sendMessage(Text.of("Refreshing...."));
        dynmap.update();
    }

    private static <T extends NamedService, S extends T> void register(String pluginId, MultiService<T> multiService, Class<S> type) {
        if (Sponge.getPluginManager().isLoaded(pluginId)) {
            try {
                S service = type.newInstance();
                if (service != null) {
                    multiService.register(service);
                    logger.info("Registered service {}", type.getSimpleName());
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("Unable to register {}", type.getSimpleName());
        }
    }
}
