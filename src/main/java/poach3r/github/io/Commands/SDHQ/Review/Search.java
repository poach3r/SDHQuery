package poach3r.github.io.Commands.SDHQ.Review;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import poach3r.github.io.Commands.AbstractCommand;
import poach3r.github.io.Commands.SDHQ.GameWrapper;
import poach3r.github.io.Config;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Search extends AbstractCommand {
    private final Config cfg;
    private final Gson gson;
    private String message;
    private String url;

    public Search(Config cfg) {
        super(Commands.slash("search", "Finds any reviews that match the given name.")
                .addOption(OptionType.STRING, "name", "The game name to find.", true));

        this.cfg = cfg;
        this.gson = new Gson();
    }

    public static String getFinalURL(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setInstanceFollowRedirects(false); // To handle redirects manually
        int statusCode = connection.getResponseCode();

        // Keep following redirects until we get a non-redirect response
        while (statusCode == HttpURLConnection.HTTP_MOVED_TEMP || statusCode == HttpURLConnection.HTTP_MOVED_PERM
                || statusCode == HttpURLConnection.HTTP_SEE_OTHER) {
            String redirectUrl = connection.getHeaderField("Location");
            if (redirectUrl == null) {
                break;
            }
            url = redirectUrl;
            connection = (HttpURLConnection) new URL(url).openConnection();
            statusCode = connection.getResponseCode();
        }

        return url;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply(false).queue();
        HttpResponse<JsonNode> response = Unirest.get("https://api.isthereanydeal.com/games/search/v1")
                .header("Content-Type", "application/json")
                .queryString("key", cfg.getItadKey())
                .queryString("title", event.getOption("name", OptionMapping::getAsString))
                .asJson();

        System.out.println(response.getBody().toPrettyString());

        Type gameItemListType = new TypeToken<List<GameWrapper.Game>>() {
        }.getType();
        List<GameWrapper.Game> gameList = gson.fromJson(response.getBody().toPrettyString(), gameItemListType);

        if (!response.isSuccess() || gameList.isEmpty()) {
            event.reply("Failed to find game.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        message = "";
        for (GameWrapper.Game game : gameList) {
            // skip non games
            if (game.getType() == null || !game.getType().equals("game"))
                continue;

            url = "https://steamdeckhq.com/game-reviews/".concat(game.getSlug());

            try {
                // check if the review actually exists
                if (getFinalURL(url).equals("https://steamdeckhq.com"))
                    continue;

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // check if the game should have its slug overridden
            if (cfg.getReviewLinkOverride() != null) {
                for (int i = 0; i < cfg.getReviewLinkOverride().get(0).size(); i++) {
                    if (cfg.getReviewLinkOverride().get(0).get(i).equals(game.getSlug())) {
                        url = "https://steamdeckhq.com/game-reviews/".concat(cfg.getReviewLinkOverride().get(1).get(i));
                        break;
                    }
                }
            }
            message = message.concat(url + "\n");
        }

        event.getHook().editOriginal(message).setSuppressEmbeds(true).queue();
    }
}
