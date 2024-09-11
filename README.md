SDHQuery is a bot created for the steamdeckhq.net Discord server.

# Commands
| Command Name  | Description                                                  | Option Names | Option Types |
|---------------|--------------------------------------------------------------|--------------|--------------|
| GetDeals      | Gets all the deals for a game from an affiliated storefront. | name         | String       |
| GetDealsById  | Gets all the deals for a game from an affiliated storefront. | id           | String       |
| GetReview     | Gets the review for a game if one is available.              | name         | String       |
| GetReviewById | Gets the review for a game if one is available.              | id           | String       |
| Search        | Finds any reviews that match the given name.                 | name         | String       |

# Configuration
| Option Name         | Description                                                                      | Expected Type |
|---------------------|----------------------------------------------------------------------------------|---------------|
| botToken            | Your Discord bot's token.                                                        | String        |
| itadKey             | Your isthereanydeal.com API key.                                                 | String        | 
| reviewLinkOverride  | Replaces the review link in the 1st array with the review link in the 2nd array. | String[][]    |
| affiliates          | All the affiliated storefront.                                                   | Affiliate[]   |
| affiliate.name      | The name of the store.                                                           | String        |
| affiliate.url       | The URL of the store.                                                            | String        |
| affiliate.suffix    | Any text to be appended to the end of the link                                   | String        |
| affiliate.overrides | Replaces the deal link in the 1st array with the deal link in the 2nd array      | String[][]    |                                       