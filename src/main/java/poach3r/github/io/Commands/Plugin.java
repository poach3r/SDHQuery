package poach3r.github.io.Commands;

import groovy.lang.Script;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class Plugin extends AbstractCommand {
    private final Script script;

    public Plugin(Script script) {
        super(Commands.slash(
                (String) script.invokeMethod("name", null),
                (String) script.invokeMethod("desc", null)));
        this.script = script;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        this.script.invokeMethod("execute", event);
    }
}
