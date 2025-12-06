package fyi.ite.ITELiveMap;

import com.destroystokyo.paper.MaterialTags;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.logging.Logger;

public class Update implements Listener {

    private static final Logger log = Bukkit.getLogger();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            Location location = new Location(event.getTo().getWorld(), event.getTo().getBlockX(), event.getTo().getWorld().getMaxHeight(), event.getTo().getBlockZ());
            Block block;
            do {
                block = location.subtract(0, 1, 0).getBlock();
            } while (block.getType() == Material.AIR);
            log.info("block is " + block.getType() + " at [" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + "]");
        }
    }

}
