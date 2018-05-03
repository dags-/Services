package me.dags.services.core;

import com.google.inject.Inject;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import me.dags.commandbus.CommandBus;
import me.dags.commandbus.annotation.Command;
import me.dags.commandbus.annotation.Permission;
import me.dags.commandbus.annotation.Src;
import me.dags.commandbus.fmt.Fmt;
import me.dags.commandbus.fmt.PagFormatter;
import me.dags.services.api.MultiService;
import me.dags.services.api.NamedService;
import me.dags.services.api.region.RegionMultiService;
import me.dags.services.api.region.RegionService;
import me.dags.services.api.warp.WarpMultiService;
import me.dags.services.api.warp.WarpService;
import me.dags.services.core.impl.bedrock.BedrockWarpService;
import me.dags.services.core.integration.Integration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.ServiceManager;
import org.spongepowered.api.text.action.TextActions;

@Plugin(name = "Services", id = "services", version = "1.2", description = "A bridge between similar, plugin-provided, services")
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
    public void postInit(GameInitializationEvent event) {
        CommandBus.create(this).register(this).submit();

        Task.builder().execute(() -> {
            // Most dependencies should be initialized by now so register services etc here
            registerService("bedrock", warpService, BedrockWarpService.class);

            registerIntegration("dynmap", "org.dynmap.DynmapCommonAPI", "me.dags.services.core.integration.dynmap.DynmapMain");
            integrations.values().forEach(Integration::init);
        }).submit(this);
    }

    @Permission("services.list")
    @Command("service list")
    public void listCommand(@Src CommandSource source) {
        PagFormatter fmt = Fmt.copy().list();
        fmt.header().stress("Services:");
        fmt.line().info("- all").command("/service refresh all");
        integrations.keySet().stream().sorted()
                .forEach(s -> fmt.line().info("- %s", s)
                        .command("/service refresh %s", s));
        fmt.build().sendTo(source);
    }

    @Permission("services.refresh")
    @Command("service refresh <id>")
    public void refreshCommand(@Src CommandSource source, String id) {
        if (id.equalsIgnoreCase("all")) {
            Fmt.info("Refreshing all services...").tell(source);
            integrations.values().forEach(Integration::update);
        } else {
            Optional<Integration> integration = getIntegration(id);
            if (integration.isPresent()) {
                Fmt.info("Refreshing service {}...", id).tell(source);
                integration.get().update();
            } else {
                Fmt.error("Could not find service {}", id).tell(source);
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
