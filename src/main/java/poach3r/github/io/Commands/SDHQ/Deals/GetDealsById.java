package poach3r.github.io.Commands.SDHQ.Deals;

import com.google.gson.Gson;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import poach3r.github.io.Commands.AbstractCommand;
import poach3r.github.io.Commands.SDHQ.GameWrapper;
import poach3r.github.io.Config;

/**
 * @author poacher
 * Lists the deals of the storefronts listed in Config for a game based on Steam ID
 */
public class GetDealsById extends AbstractCommand {
    private final Gson gson;
    private final Config cfg;
    private final EmbedBuilder embedBuilder;
    private final MessageCreateBuilder messageBuilder;
    private String embedContent;

    public GetDealsById(Config cfg) {
        super(Commands.slash("getdealsbyid", "Lists any deals for a game from an affiliated store.")
                .addOption(OptionType.STRING, "id", "The ID of the game.", true));

        gson = new Gson();
        this.cfg = cfg;

        embedBuilder = new EmbedBuilder();
        embedBuilder.setFooter("Info is queried from isthereanydeal.com.\nSDHQ is not affiliated with isthereanydeal.com.");

        messageBuilder = new MessageCreateBuilder();

        embedContent = "";
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

        embedBuilder.setTitle(gameWrapper.getGame().getTitle());

        // get deal info from itad using the queried itad id
        response = Unirest.post("https://api.isthereanydeal.com/games/prices/v2")
                .header("Content-Type", "application/json")
                .queryString("key", cfg.getItadKey())
                .queryString("nondeals", true)
                .body("[ \"" + gameWrapper.getGame().getId() + "\" ]")
                .asJson();

        AvailableStorefronts[] availableStorefronts = gson.fromJson(response.getBody().toPrettyString(), AvailableStorefronts[].class);

        // creates the message part for each storefront
        // TODO make this not loop through already checked storefronts
        for (AvailableStorefronts store : availableStorefronts) {
            for (Deal deal : store.deals) {
                // check if the game is being sold by affiliates
                for (Config.Affiliate affiliate : cfg.getAffiliates()) {
                    if (deal.shop.name.equals(affiliate.getName())) {
                        // create message part
                        if (affiliate.getOverrides() != null) {
                            for (int i = 0; i < affiliate.getOverrides().get(0).size(); i++) {
                                if (affiliate.getOverrides().get(0).get(i).equals(gameWrapper.getGame().getSlug())) {
                                    embedContent = embedContent.concat("## [" + deal.shop.name + "](" + affiliate.getUrl() + affiliate.getOverrides().get(1).get(i) + affiliate.getSuffix() + ")\n" + deal.price.amount + " " + deal.price.currency + "\n");
                                    break;
                                }
                            }
                        } else
                            embedContent = embedContent.concat("## [" + deal.shop.name + "](" + affiliate.getUrl() + gameWrapper.getGame().getSlug() + affiliate.getSuffix() + ")\n" + deal.price.amount + " " + deal.price.currency + "\n");
                    }
                }
            }
        }

        embedBuilder.appendDescription(embedContent);

        messageBuilder.clear();

        event.reply(messageBuilder
                .addEmbeds(embedBuilder.build())
                .build()
        ).queue();
    }
}