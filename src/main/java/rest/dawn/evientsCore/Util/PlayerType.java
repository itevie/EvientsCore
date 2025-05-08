package rest.dawn.evientsCore.Util;

public enum PlayerType {
    ALL,
    RANDOMALIVE,
    RANDOMDEAD,
    ALIVE,
    DEAD,
    RANDOM;

    public static PlayerType getFromString(String from) {
        for (PlayerType type : values()) {
            if (from.toLowerCase().endsWith(type.name().toLowerCase())) {
                return type;
            }
        }

        throw new IllegalArgumentException("No PlayerType matching the provided name");
    }

    public String toHumanString() {
        return switch (this) {
            case PlayerType.ALL -> "all players";
            case PlayerType.DEAD -> "all dead players";
            case PlayerType.ALIVE -> "all alive players";
            case PlayerType.RANDOM -> "a random player";
            case PlayerType.RANDOMALIVE -> "a random alive player";
            case PlayerType.RANDOMDEAD -> "a random dead player";
        };
    }
}
