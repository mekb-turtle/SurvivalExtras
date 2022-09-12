package tech.mekb.survivalextras;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;
import static tech.mekb.survivalextras.SurvivalExtras.getArgString;
import static tech.mekb.survivalextras.SurvivalExtras.replaceColours;

public class LoreCommand implements CommandExecutor {
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
                if (args.length < 1) {
                    sender.sendMessage("§cUsage: " + cmd.getUsage());
                    return true;
                }
                List<String> lore = im.getLore();
                if (args[0].equals("add")) {
                    if (lore == null) {
                        lore = new ArrayList<>();
                    }
                    String str = replaceColours(getArgString(args, 1));
                    lore.add(str);
                    sender.sendMessage("Added new lore: " + str);
                } else if (args[0].equals("remove") && args.length == 2) {
                    if (lore != null) {
                        try {
                            int i = parseInt(args[1]);
                            if (i >= 0 && i < lore.size()) {
                                sender.sendMessage("Removed lore at " + i + ": " + lore.get(i));
                                lore.remove(i);
                            } else {
                                sender.sendMessage("§cInteger " + i + " out of bounds");
                                return true;
                            }
                        } catch (NumberFormatException e) {
                            sender.sendMessage("§cNot a valid integer");
                            return true;
                        }
                    } else {
                        sender.sendMessage("§cNo lore");
                        return true;
                    }
                } else if ((args[0].equals("insert") || args[0].equals("set")) && args.length >= 2) {
                    if (lore != null) {
                        try {
                            int i = parseInt(args[1]);
                            String str = replaceColours(getArgString(args, 2));
                            if (i >= 0 && i < lore.size()) {
                                if (args[0].equals("set")) {
                                    sender.sendMessage("Set lore at " + i + ": " + str);
                                    lore.set(i, str);
                                } else {
                                    sender.sendMessage("Inserted lore before " + i + ": " + str);
                                    lore.add(i, str);
                                }
                            } else {
                                sender.sendMessage("§cInteger " + i + " out of bounds");
                                return true;
                            }
                        } catch (NumberFormatException e) {
                            sender.sendMessage("§cNot a valid integer");
                            return true;
                        }
                    } else {
                        sender.sendMessage("§cNo lore");
                        return true;
                    }
                } else if (args[0].equals("list") && args.length == 1) {
                    if (lore != null) {
                        sender.sendMessage("Lore:");
                        List<String> lines = im.getLore();
                        for (int i = 0; i < lines.size(); ++i) {
                            sender.sendMessage("§7" + i + ". §r" + lines.get(i));
                        }
                    } else {
                        sender.sendMessage("§cNo lore");
                        return true;
                    }
                } else if (args[0].equals("clear") && args.length == 1) {
                    if (lore == null) {
                        sender.sendMessage("§cNo lore");
                        return true;
                    } else {
                        im.setLore(null);
                        sender.sendMessage("Cleared lore");
                    }
                } else {
                    sender.sendMessage("§cUsage: " + cmd.getUsage());
                    return true;
                }
            }
            item.setItemMeta(im);
            return true;
        }
        sender.sendMessage("§cYou need to be a player to run this command");
        return true;
    }
}
