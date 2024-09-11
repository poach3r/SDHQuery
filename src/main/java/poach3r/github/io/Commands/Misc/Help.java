package poach3r.github.io.Commands.Misc;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import poach3r.github.io.Commands.AbstractCommand;
import poach3r.github.io.Config;

public class Help extends AbstractCommand {
    private final Config cfg;
    private final EmbedBuilder embedBuilder;
    private final MessageCreateBuilder messageBuilder;
    private final String embedContent;

    public Help(Config cfg) {
        super(Commands.slash("help", "Shows all available commands and their descriptions."));
        this.cfg = cfg;
        this.embedBuilder = new EmbedBuilder();
        this.messageBuilder = new MessageCreateBuilder();
        this.embedContent = "";

        embedBuilder.setTitle("help");
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
    }
}
