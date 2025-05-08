package rest.dawn.evientsCore.Managers;

import rest.dawn.evientsCore.EvientsCore;
import rest.dawn.evientsCore.Util.UndoAction;

import javax.annotation.Nullable;

public class StateManager {
    private final EvientsCore plugin;
    public boolean chatMuted = false;
    public boolean rejoinsDisabled = false;
    public @Nullable UndoAction undo = null;

    public StateManager(EvientsCore plugin) {
        this.plugin = plugin;
    }
}
