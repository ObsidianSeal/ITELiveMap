package fyi.ite.ITELiveMap;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static fyi.ite.ITELiveMap.ITELiveMap.sendMapRegion;

public class Update implements Listener {
    
    final int REGION_SIZE = 16;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        World world = event.getPlayer().getWorld();

        int fromX = event.getFrom().getBlockX();
        int fromZ = event.getFrom().getBlockZ();

        if (fromX < 0 && fromZ >= 0) {
            fromX -= 15;
        }
        if (fromX >= 0 && fromZ < 0) {
            fromZ -= 15;
        }
        if (fromX < 0 && fromZ < 0) {
            fromX -= 15; fromZ -= 15;
        }

        int toX = event.getTo().getBlockX();
        int toZ = event.getTo().getBlockZ();

        if (toX < 0 && toZ >= 0) {
            toX -= 15;
        }
        if (toX >= 0 && toZ < 0) {
            toZ -= 15;
        }
        if (toX < 0 && toZ < 0) {
            toX -= 15; toZ -= 15;
        }

        fromX /= REGION_SIZE; fromZ /= REGION_SIZE;
        toX /= REGION_SIZE; toZ /= REGION_SIZE;

        if (fromX != toX || fromZ != toZ) getMapRegion(world, toX, toZ);
    }

    public void getMapRegion(World world, int chunkX, int chunkZ) {
//        for (Player player : world.getPlayers()) player.sendMessage("PlayerMoveEvent triggered for " + chunkX + ", " + chunkZ);

        StringBuilder blockString = new StringBuilder();

        for (int x = chunkX * REGION_SIZE; x < chunkX * REGION_SIZE + REGION_SIZE; x++) {
            for (int z = chunkZ * REGION_SIZE; z < chunkZ * REGION_SIZE + REGION_SIZE; z++) {
                Location location = new Location(world, x, world.getMaxHeight(), z);
                Location testLocation = location.clone(); testLocation.subtract(0, 0, 1);
                Block block, testBlock;
                Color blockColour;
                int shadeFactor;

                int mainY;
                do {
                    block = location.subtract(0, 1, 0).getBlock();
                    mainY = location.getBlockY();
                } while (block.getType() == Material.AIR || (block.getBlockData().getMapColor().getRed() == 0 && block.getBlockData().getMapColor().getGreen() == 0 && block.getBlockData().getMapColor().getBlue() == 0) && mainY > world.getMinHeight());

                if (block.getType() != Material.WATER) {
                    int testY;
                    do {
                        testBlock = testLocation.subtract(0, 1, 0).getBlock();
                        testY = testLocation.getBlockY();
                    } while (testBlock.getType() == Material.AIR || (testBlock.getBlockData().getMapColor().getRed() == 0 && testBlock.getBlockData().getMapColor().getGreen() == 0 && testBlock.getBlockData().getMapColor().getBlue() == 0) && testY > world.getMinHeight());

                    blockColour = block.getBlockData().getMapColor();
                    shadeFactor = 220;
                    if (testY > mainY) shadeFactor = 180;
                    if (testY < mainY) shadeFactor = 255;
                } else {
                    int depth = 0;
                    do {
                        block = location.subtract(0, 1, 0).getBlock();
                        depth++;
                    } while (block.getType() == Material.WATER || (block.getBlockData().getMapColor().getRed() == 0 && block.getBlockData().getMapColor().getGreen() == 0 && block.getBlockData().getMapColor().getBlue() == 0) && block.getY() > world.getMinHeight());

                    shadeFactor = 180;
                    if (depth == 1 || depth == 2) {
                        shadeFactor = 255;
                    }
                    if (depth == 3 || depth == 4) {
                        if (block.getX() % 2 == 0 && block.getZ() % 2 == 0 || block.getX() % 2 != 0 && block.getZ() % 2 != 0) shadeFactor = 255;
                        else shadeFactor = 220;
                    }
                    if (depth == 5 || depth == 6) {
                        shadeFactor = 220;
                    }
                    if (depth == 7 || depth == 8 || depth == 9) {
                        if (block.getX() % 2 == 0 && block.getZ() % 2 == 0 || block.getX() % 2 != 0 && block.getZ() % 2 != 0) shadeFactor = 220;
                    }

                    blockColour = location.add(0, 1, 0).getBlock().getBlockData().getMapColor();
                }

                String red = String.format("%2s", Integer.toHexString(blockColour.getRed() * shadeFactor / 255)).replace(" ", "0");
                String green = String.format("%2s", Integer.toHexString(blockColour.getGreen() * shadeFactor / 255)).replace(" ", "0");
                String blue = String.format("%2s", Integer.toHexString(blockColour.getBlue() * shadeFactor / 255)).replace(" ", "0");

                blockString.append(red).append(green).append(blue);
            }
        }
        sendMapRegion(chunkX + "," + chunkZ, blockString.toString()); // send to Firebase Realtime Database
    }

}
