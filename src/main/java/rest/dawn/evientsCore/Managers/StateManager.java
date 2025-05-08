package rest.dawn.evientsCore.Managers;

import rest.dawn.evientsCore.EvientsCore;

public class StateManager {
    private final EvientsCore plugin;
    public boolean chatMuted = false;
    public boolean rejoinsDisabled = false;

    public StateManager(EvientsCore plugin) {
        this.plugin = plugin;
    }
}
