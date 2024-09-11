package poach3r.github.io.Commands.SDHQ;

/**
 * @author poacher
 */
public class GameWrapper {
    private boolean found;
    private Game game;

    public boolean isFound() {
        return found;
    }

    public Game getGame() {
        return game;
    }

    public static class Game {
        private String id;
        private String title;
        private String slug;
        private String type;

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getSlug() {
            return slug;
        }

        public String getType() {
            return type;
        }
    }
}

