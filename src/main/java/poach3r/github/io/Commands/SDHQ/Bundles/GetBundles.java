package poach3r.github.io.Commands.SDHQ.Bundles;

import com.google.gson.Gson;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import poach3r.github.io.Commands.AbstractCommand;
import poach3r.github.io.Config;

public class GetBundles extends AbstractCommand {
    private final Gson gson;
    private final Config cfg;

    public GetBundles(Config cfg) {
        super(Commands.slash("getbundles", "Lists all the bundles from Humble Bundle and Fanatical."));
        this.gson = new Gson();
        this.cfg = cfg;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
    }
}
