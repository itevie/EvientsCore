package rest.dawn.evientsCore.Util;

public enum HideMode {
    Staff,
    All,
    Off;

    public static HideMode fromCommandString(String commandString) {
        return switch (commandString) {
            case "all" -> HideMode.All;
            case "staff" -> HideMode.Staff;
            case "off" -> HideMode.Off;
            default -> throw new IllegalArgumentException();
        };
    }

    public String toHumanString() {
        return switch (this) {
            case HideMode.All -> "everyone";
            case HideMode.Off -> "no-one";
            case HideMode.Staff -> "everyone but staff";
        };
    }
}
