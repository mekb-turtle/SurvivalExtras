package tech.mekb.survivalextras;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static tech.mekb.survivalextras.SurvivalExtras.getArgString;
import static tech.mekb.survivalextras.SurvivalExtras.replaceColours;

public class RenameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender,
                           Command cmd, String label,
                           String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            ItemStack item = p.getInventory().getItemInMainHand();
            if (item.getType() == Material.AIR) {
                item = p.getInventory().getItemInOffHand();
                if (item.getType() == Material.AIR) {
                    sender.sendMessage("§cYou are not holding anything");
                    return true;
                }
            }
            ItemMeta im = item.getItemMeta();
            if (im != null) {
                im.setDisplayName(args.length == 0 ? null : replaceColours(getArgString(args, 0)));
                item.setItemMeta(im);
            }
            return true;
        }
        sender.sendMessage("§cYou need to be a player to run this command");
        return true;
    }
}
