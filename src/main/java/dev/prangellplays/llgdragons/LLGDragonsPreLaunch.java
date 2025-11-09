package dev.prangellplays.llgdragons;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class LLGDragonsPreLaunch implements PreLaunchEntrypoint {

    @Override
    public void onPreLaunch() {
        System.setProperty("devauth.enabled", "true");
        System.setProperty("devauth.account", "main");
    }
}
