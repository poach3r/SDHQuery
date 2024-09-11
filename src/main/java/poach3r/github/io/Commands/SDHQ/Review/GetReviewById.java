package poach3r.github.io.Commands.SDHQ.Review;

import com.google.gson.Gson;
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
import java.net.HttpURLConnection;
import java.net.URL;

public class GetReviewById extends AbstractCommand {
    private final Config cfg;
    private final Gson gson;
    private String url;

    public GetReviewById(Config cfg) {
        super(Commands.slash("getreviewbyid", "Gets the review for a game if one is available.")
                .addOption(OptionType.STRING, "id", "The ID of the game.", true));
        this.cfg = cfg;
        this.gson = new Gson();
        this.url = "";
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
        // get game info from itad using the provided id
        HttpResponse<JsonNode> response = Unirest.get("https://api.isthereanydeal.com/games/lookup/v1")
                .header("Content-Type", "application/json")
                .queryString("key", cfg.getItadKey())
                .queryString("appid", event.getOption("id", OptionMapping::getAsString))
                .asJson();

        GameWrapper gameWrapper = gson.fromJson(response.getBody().toPrettyString(), GameWrapper.class);

        // if no game is found
        if (!response.isSuccess() || !gameWrapper.isFound()) {
            event.reply("Failed to find game.")
                    .setEphemeral(true)
                    .queue();
            return;
        }

        url = "https://steamdeckhq.com/game-reviews/".concat(gameWrapper.getGame().getSlug());

        // check if the game should have its slug overridden
        if (cfg.getReviewLinkOverride() != null) {
            for (int i = 0; i < cfg.getReviewLinkOverride().get(0).size(); i++) {
                if (cfg.getReviewLinkOverride().get(0).get(i).equals(gameWrapper.getGame().getSlug())) {
                    url = "https://steamdeckhq.com/game-reviews/".concat(cfg.getReviewLinkOverride().get(1).get(i));
                    break;
                }
            }
        }

        try {
            // check if the review actually exists
            if (getFinalURL(url).equals("https://steamdeckhq.com")) {
                event.reply("No review found.")
                        .setEphemeral(true)
                        .queue();
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        event.reply(url)
                .queue();
    }
}
