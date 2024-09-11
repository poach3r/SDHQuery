package poach3r.github.io;

import com.google.gson.Gson;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import poach3r.github.io.Commands.AbstractCommand;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Scanner;

/**
 * @author poach3r
 */
public class sdhQuery {
    protected final Config cfg;
    protected final CommandListUpdateAction commands;
    protected final JDA jda;
    private final Gson gson;
    protected String cfgString;

    public sdhQuery() {
        gson = new Gson();
        cfgString = "";

        try {
            Scanner myReader = new Scanner(new File("config.json"));
            while (myReader.hasNextLine()) {
                cfgString = cfgString.concat(myReader.nextLine() + "\n");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        cfg = gson.fromJson(cfgString, Config.class);

        jda = JDABuilder.createLight(cfg.getBotToken(), Collections.emptyList())
                .addEventListeners(new SlashCommandListener(this))
                .build();

        commands = jda.updateCommands();

        commands.addCommands(cfg.getCommands().stream().map(AbstractCommand::get).toList());

        commands.queue();
    }

    public static void main(String[] args) {
        new sdhQuery();
    }
}