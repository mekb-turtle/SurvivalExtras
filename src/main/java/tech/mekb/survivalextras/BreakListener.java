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
import org.bukkit.inventory.PlayerInventory;
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
        PlayerInventory inv = p.getInventory();
        ItemStack item = inv.getItemInMainHand();
        Material itype = item.getType();
        switch (itype) {
            case DIAMOND_HOE:
            case GOLDEN_HOE:
            case IRON_HOE:
            case STONE_HOE:
            case WOODEN_HOE:
            case NETHERITE_HOE:
                Block b = e.getBlock();
                Material btype = b.getType();
                switch (btype) {
                    case WHEAT:
                    case BEETROOTS:
                    case CARROTS:
                        BlockData bd = b.getBlockData();
                        if (bd instanceof Ageable) {
                            Ageable ba = (Ageable) bd;
                            if (ba.getAge() == ba.getMaximumAge()) {
                                Material im = null;
                                switch (btype) {
                                    case WHEAT:
                                        im = Material.WHEAT_SEEDS;
                                        break;
                                    case BEETROOTS:
                                        im = Material.BEETROOT_SEEDS;
                                        break;
                                    case CARROTS:
                                        im = Material.CARROT;
                                        break;
                                    default:
                                        return;
                                }
                                boolean a = false;
                                for (int i = 0; i < 36; ++i) {
                                    ItemStack it = inv.getItem(i);
                                    if (it == null) continue;
                                    if (it.getType() != im) continue;
                                    int s = it.getAmount() - 1;
                                    a = true;
                                    if (s <= 0) {
                                        inv.setItem(i, new ItemStack(Material.AIR));
                                    } else {
                                        it.setAmount(s);
                                        inv.setItem(i, it);
                                    }
									break;
                                }
                                if (!a) return;
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
                        break;
                }
                break;
        }
    }
}
