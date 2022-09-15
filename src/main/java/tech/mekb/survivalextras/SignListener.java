package tech.mekb.survivalextras;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {
    @EventHandler
    public void use(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player p = e.getPlayer();
        if (p.isSneaking()) return;
        Block b = e.getClickedBlock();
        if (b == null) return;
        BlockState bs = b.getState();
        if (bs instanceof Sign) {
            e.getPlayer().openSign((Sign)bs);
        }
    }
}
