import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

def name() { "helloworld" }
def desc() { "An example plugin which makes the bot say \"Hello, world!\"" }

def execute(SlashCommandInteractionEvent event) {
    event.reply("Hello, world!").queue();
}