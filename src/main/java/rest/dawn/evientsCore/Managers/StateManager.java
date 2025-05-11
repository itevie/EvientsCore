package rest.dawn.evientsCore.Managers;

import rest.dawn.evientsCore.Util.UndoAction;

import javax.annotation.Nullable;

public class StateManager {
    public boolean chatMuted = false;
    public boolean rejoinsDisabled = false;
    public @Nullable UndoAction undo = null;
}
