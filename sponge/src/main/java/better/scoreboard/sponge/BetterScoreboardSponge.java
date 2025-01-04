package better.scoreboard.sponge;

import better.scoreboard.core.BetterScoreboard;
import better.scoreboard.core.placeholder.PlaceholderManager;
import better.scoreboard.sponge.bridge.SpongeConfigSection;
import better.scoreboard.sponge.bridge.SpongePlaceholderProcessor;
import better.scoreboard.sponge.bridge.SpongePluginLogger;
import better.scoreboard.sponge.bridge.SpongeUserData;
import better.scoreboard.sponge.listener.PlayerUpdateListener;
import better.scoreboard.sponge.listener.ReloadListener;
import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.bstats.sponge.Metrics;
import org.spongepowered.api.Game;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.StartedEngineEvent;
import org.spongepowered.api.event.lifecycle.StartingEngineEvent;
import org.spongepowered.api.event.lifecycle.StoppingEngineEvent;
import org.spongepowered.api.network.ServerConnectionState;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import org.spongepowered.plugin.PluginContainer;
import org.spongepowered.plugin.builtin.jvm.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Plugin("betterscoreboard")
public class BetterScoreboardSponge {

    private static final int B_STATS_ID = 24202;

    // Core objects.
    private BetterScoreboard core;

    // Sponge injection objects.

    private final Game game;
    private final Logger logger;
    private final PluginContainer pluginContainer;
    private final Metrics metrics;

    @Inject
    @ConfigDir(sharedRoot = true)
    private Path configDirectory;

    @Inject
    public BetterScoreboardSponge(Game game, Logger logger, PluginContainer pluginContainer, Metrics.Factory metrics) {
        this.game = game;
        this.logger = logger;
        this.pluginContainer = pluginContainer;
        this.metrics = metrics.make(B_STATS_ID);
    }

    @Listener
    public void onServerLoad(final StartingEngineEvent<Server> event) {
        core = new BetterScoreboard(
                new SpongePlaceholderProcessor(this),
                new SpongePluginLogger(logger),
                new SpongeUserData(game)
        );

        core.init();

        PlaceholderManager.registerPlaceholder("displayname", user -> {
            Optional<ServerPlayer> player = game.server().player(user.getUUID());
            if (player.isEmpty()) return "";
            return player.get().displayName().get().toString();
        });
        PlaceholderManager.registerPlaceholder("gamemode", user -> {
            Optional<ServerPlayer> player = game.server().player(user.getUUID());
            if (player.isEmpty()) return "";
            return String.valueOf(player.get().gameMode().get().toString());
        });
        PlaceholderManager.registerPlaceholder("health", user -> {
            Optional<ServerPlayer> player = game.server().player(user.getUUID());
            if (player.isEmpty()) return "";
            return String.valueOf(player.get().health().get());
        });
        PlaceholderManager.registerPlaceholder("maxplayers", user -> String.valueOf(game.server().maxPlayers()));
        PlaceholderManager.registerPlaceholder("ping", user -> {
            // The API is missing an easy way to get latency. Thank you gabizou for this work around.   `
            Optional<ServerPlayer> player = game.server().player(user.getUUID());
            if (player.isEmpty()) return "";
            return String.valueOf(player.get().connection().state()
                    .filter(e -> e instanceof ServerConnectionState.Game)
                    .map(e -> (ServerConnectionState.Game) e)
                    .map(ServerConnectionState.Game::latency).orElse(0));
        });
        PlaceholderManager.registerPlaceholder("players", user -> String.valueOf(game.server().onlinePlayers().size()));
        PlaceholderManager.registerPlaceholder("world", user -> {
            Optional<ServerPlayer> player = game.server().player(user.getUUID());
            return player.map(serverPlayer -> serverPlayer.world().key().value()).orElse("");
        });
        PlaceholderManager.registerPlaceholder("worldplayers", user -> {
            Optional<ServerPlayer> player = game.server().player(user.getUUID());
            return player.map(serverPlayer -> String.valueOf(serverPlayer.world().players().size())).orElse("");
        });
    }

    @Listener
    public void onServerStart(final StartedEngineEvent<Server> event) {
        core.enable();

        // Register all listeners.
        Sponge.eventManager().registerListeners(pluginContainer, new PlayerUpdateListener());
        Sponge.eventManager().registerListeners(pluginContainer, new ReloadListener(this));

        load();

        // General
        Task task = Task.builder().delay(Ticks.single()).interval(Ticks.single()).plugin(pluginContainer).execute(core::tick).build();
        Sponge.asyncScheduler().submit(task);
    }

    @Listener
    public void onServerStop(final StoppingEngineEvent<Server> event) {
        core.disable();

        metrics.shutdown();
    }

    public void load() {
        try {
            // This shouldn't be possible, but check anyway.
            if (!Files.exists(configDirectory)) Files.createDirectories(configDirectory);

            Path filePath = configDirectory.resolve("BetterScoreboardConfig.yml");
            File file = filePath.toFile();

            if (!file.exists()) {
                InputStream inputStream = BetterScoreboard.class.getResourceAsStream("/config.yml");
                assert (inputStream != null);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            YamlConfigurationLoader configLoader = YamlConfigurationLoader.builder().nodeStyle(NodeStyle.BLOCK).path(filePath).build();
            CommentedConfigurationNode node = configLoader.load();

            core.load(new SpongeConfigSection(node));
        } catch (IOException e) {
            logger.warn("Could not load BetterScoreboard's configuration.");
            logger.warn("Please verify the legitimacy of your configuration file as the plugin may not work as intended.");
            e.printStackTrace();
        }
    }
}
