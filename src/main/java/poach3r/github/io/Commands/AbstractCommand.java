package poach3r.github.io.Commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

/**
 * @author poach3r
 */
public abstract class AbstractCommand {
    private final CommandData command;

    public AbstractCommand(CommandData command) {
        this.command = command;
    }

    public final CommandData get() {
        return command;
    }

    public abstract void execute(SlashCommandInteractionEvent event);
}
