package rest.dawn.evientsCore.Managers;

import rest.dawn.evientsCore.EvientsCore;

public class StateManager {
    public boolean chatMuted = false;

    EvientsCore plugin;

    public StateManager(EvientsCore plugin) {
        this.plugin = plugin;
    }
}
