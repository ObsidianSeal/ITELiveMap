package fyi.ite.ITELiveMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class ITELiveMap extends JavaPlugin {

    private static final Logger log = Bukkit.getLogger();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Update(), this);
        log.info("HELLO. ITELiveMap NOW ENABLED.");
    }

    @Override
    public void onDisable() {
        log.info("FAREWELL. ITELiveMap NOW DISABLED.");
    }
}
