package tech.mekb.survivalextras;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;

public class AnvilListener implements Listener {
    long lastCalled = System.currentTimeMillis();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void anvilCost(PrepareAnvilEvent event) {
        AnvilInventory inv = event.getInventory();
        inv.setMaximumRepairCost(Integer.MAX_VALUE);
        if (event.getResult() != null && System.currentTimeMillis() - lastCalled > 10 && inv.getRepairCost() >= 40) {
            lastCalled = System.currentTimeMillis();
            Player p = (Player) event.getViewers().get(0);
            p.sendMessage("§cCost: " + inv.getRepairCost() + " levels");
        }
    }
}