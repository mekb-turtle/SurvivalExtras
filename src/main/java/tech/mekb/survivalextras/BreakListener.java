package tech.mekb.survivalextras;

import jdk.jfr.internal.LogLevel;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

import static org.bukkit.Bukkit.getServer;

public class BreakListener implements Listener {
    BreakListener(SurvivalExtras main) {
        this.main = main;
    }

    public SurvivalExtras main;

    @EventHandler
    public void breakBlock(BlockBreakEvent e) {
        Player p = e.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        Material itype = item.getType();
        if (itype == Material.DIAMOND_HOE || itype == Material.GOLDEN_HOE || itype == Material.IRON_HOE || itype == Material.STONE_HOE || itype == Material.WOODEN_HOE || itype == Material.NETHERITE_HOE) {
            Block b = e.getBlock();
            Material btype = b.getType();
            if (btype == Material.WHEAT || btype == Material.WHEAT_SEEDS || btype == Material.BEETROOT || btype == Material.BEETROOT_SEEDS || btype == Material.CARROT || btype == Material.CARROTS) {
                BlockData bd = b.getBlockData();
                if (bd instanceof Ageable) {
                    Ageable ba = (Ageable)bd;
                    if (ba.getAge() == ba.getMaximumAge()) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (b.getType() == Material.AIR) {
                                    ba.setAge(0);
                                    b.setType(btype);
                                    b.setBlockData(ba);
                                }
                            }
                        }.runTaskLater(main, 1L);
                    } else {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
