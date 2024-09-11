package poach3r.github.io;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import poach3r.github.io.Commands.AbstractCommand;

/**
 * @author poach3r
 */
public class SlashCommandListener extends ListenerAdapter {
    private final pBot pBot;

    public SlashCommandListener(pBot pBot) {
        this.pBot = pBot;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        for (AbstractCommand c : pBot.cfg.getCommands()) {
            if (c.get().getName().equals(event.getName())) {
                c.execute(event);
                return;
            }
        }
    }
}