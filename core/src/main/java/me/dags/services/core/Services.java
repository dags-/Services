package me.dags.services.core;

import com.google.inject.Inject;
import me.dags.commandbus.CommandBus;
import me.dags.commandbus.annotation.Caller;
import me.dags.commandbus.annotation.Command;
import me.dags.commandbus.annotation.One;
import me.dags.commandbus.annotation.Permission;
import me.dags.commandbus.utils.Format;
import me.dags.services.api.MultiService;
import me.dags.services.api.NamedService;
import me.dags.services.api.region.RegionMultiService;
import me.dags.services.api.region.RegionService;
import me.dags.services.api.warp.WarpMultiService;
import me.dags.services.api.warp.WarpService;
import me.dags.services.core.impl.bedrock.BedrockWarpService;
import me.dags.services.core.impl.safeguard.SafeGuardRegionService;
import me.dags.services.core.integration.Integration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.ServiceManager;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Plugin(name = "Services", id = "services", version = "1.1")
public class Services {

    private static final Logger logger = LoggerFactory.getLogger("Services");
    private static final RegionMultiService regionService = new RegionMultiService();
    private static final WarpMultiService warpService = new WarpMultiService();

    private final Map<String, Integration> integrations = new HashMap<>();

    private static Services instance;
    public final Path configDir;

    @Inject
    public Services(@ConfigDir(sharedRoot = false) Path configDir) {
        this.configDir = configDir;
        Services.instance = this;
    }

    @Listener (order = Order.FIRST)
    public void preInit(GamePreInitializationEvent event) {
        ServiceManager manager = Sponge.getServiceManager();
        manager.setProvider(this, RegionMultiService.class, regionService);
        manager.setProvider(this, WarpMultiService.class, warpService);
        manager.setProvider(this, RegionService.class, regionService);
        manager.setProvider(this, WarpService.class, warpService);
    }

    @Listener
    public void postInit(GamePostInitializationEvent event) {
        CommandBus.builder().logger(logger).build().register(this).submit(this);

        // Most dependencies should be initialized by now so register services etc here
        registerService("bedrock", warpService, BedrockWarpService.class);
        registerService("safeguard", regionService, SafeGuardRegionService.class);

        registerIntegration("dynmap", "org.dynmap.DynmapCommonAPI", "me.dags.services.core.integration.dynmap.DynmapMain");
        integrations.values().forEach(Integration::init);
    }

    @Command(aliases = "refresh", parent = "service", perm = @Permission("services.refresh"))
    public void refreshCommand(@Caller CommandSource source, @One String id) {
        if (id.equalsIgnoreCase("all")) {
            Format.DEFAULT.info("Refreshing all services...").tell(source);
            integrations.values().forEach(Integration::update);
        } else {
            Optional<Integration> integration = getIntegration(id);
            if (integration.isPresent()) {
                Format.DEFAULT.info("Refreshing {}...", id).tell(source);
                integration.get().update();
            } else {
                Format.DEFAULT.error("Could not find {}", id).tell(source);
            }
        }
    }

    public static Services getInstance() {
        return instance;
    }

    private Optional<Integration> getIntegration(String identifier) {
        return Optional.ofNullable(integrations.get(identifier));
    }

    private void registerIntegration(String identifier, String lookUpClass, String initClass) {
        Integration integration = new Integration(logger, lookUpClass, initClass);
        integrations.put(identifier, integration);
    }

    private static <T extends NamedService, S extends T> void registerService(String pluginId, MultiService<T> multiService, Class<S> type) {
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
