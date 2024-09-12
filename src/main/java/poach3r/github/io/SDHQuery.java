package poach3r.github.io;

import com.google.gson.Gson;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import poach3r.github.io.Commands.AbstractCommand;
import poach3r.github.io.Commands.Plugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;

/**
 * @author poach3r
 */
public class SDHQuery {
    protected final Config cfg;
    protected final CommandListUpdateAction commands;
    protected final JDA jda;
    private final PluginLoader pluginLoader;
    protected String cfgString;

    public SDHQuery() throws IOException {
        cfgString = "";
        cfgString = String.join("", Files.readAllLines(new File("config.json").toPath()));
        cfg = new Gson().fromJson(cfgString, Config.class);

        jda = JDABuilder.createLight(cfg.getBotToken(), Collections.emptyList())
                .addEventListeners(new SlashCommandListener(this))
                .build();

        pluginLoader = new PluginLoader(cfg.getEnabledPlugins());

        commands = jda.updateCommands();
        loadCommands(this);
    }

    private void loadCommands(SDHQuery sdhQuery) throws IOException {
        pluginLoader.load();
        pluginLoader.getPlugins().forEach(cfg::addCommand);
        commands.addCommands(cfg.getCommands().stream().map(AbstractCommand::get).toList());
        commands.queue();
    }

    public static void main(String[] args) throws IOException {
        new SDHQuery();
    }
}