package tech.mekb.survivalextras;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;
import static tech.mekb.survivalextras.SurvivalExtras.getArgString;
import static tech.mekb.survivalextras.SurvivalExtras.replaceColours;

public class LoreCommand implements CommandExecutor {
    int loreLimit = 20;

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
            if (args.length < 1) {
                sender.sendMessage("§cUsage: " + cmd.getUsage());
                return true;
            }
            boolean isAdd = args[0].equals("add");
            boolean isRemove = args[0].equals("remove") && args.length == 2;
            boolean isInsert = args[0].equals("insert") && args.length >= 2;
            boolean isSet = args[0].equals("set") && args.length >= 2;
            boolean isList = args[0].equals("list") && args.length == 1;
            boolean isClear = args[0].equals("clear") && args.length == 1;
            ItemMeta im = item.getItemMeta();
            if (im != null) {
                List<String> lore = im.getLore();
                if (isAdd) {
                    if (lore == null) {
                        lore = new ArrayList<>();
                    }
                    if (lore.size() >= loreLimit) { // stop
                        sender.sendMessage("§cMaximum of " + loreLimit + " lines of lore");
                    } else {
                        String str = replaceColours(getArgString(args, 1));
                        lore.add(str);
                        sender.sendMessage("§fAdded new lore: " + str);
                        im.setLore(lore);
                    }
                } else if (isRemove) {
                    if (lore != null) {
                        try {
                            int i = parseInt(args[1])-1;
                            if (i >= 0 && i < lore.size()) {
                                sender.sendMessage("§fRemoved lore at " + i + ": " + lore.get(i));
                                lore.remove(i);
                                if (lore.size() == 0) lore = null;
                                im.setLore(lore);
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
                } else if (isSet || isInsert) {
                    if (isInsert && lore == null) {
                        lore = new ArrayList<>();
                    }
                    if (lore != null) {
                        try {
                            int i = parseInt(args[1])-1;
                            String str = replaceColours(getArgString(args, 2));
                            if (i >= 0 && i < lore.size()) {
                                if (isSet) {
                                    sender.sendMessage("§fSet lore at " + i + ": " + str);
                                    lore.set(i, str);
                                } else {
                                    if (lore.size() >= loreLimit) { // no
                                        sender.sendMessage("§cMaximum of " + loreLimit + " lines of lore");
                                    } else {
                                        sender.sendMessage("§fInserted lore before " + i + ": " + str);
                                        lore.add(i, str);
                                    }
                                }
                                im.setLore(lore);
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
                } else if (isList) {
                    if (lore != null) {
                        sender.sendMessage("§fLore:");
                        List<String> lines = im.getLore();
                        for (int i = 0; i < lines.size(); ++i) {
                            sender.sendMessage("§7" + (i+1) + ". §r" + lines.get(i));
                        }
                    } else {
                        sender.sendMessage("§cNo lore");
                        return true;
                    }
                } else if (isClear) {
                    if (lore == null) {
                        sender.sendMessage("§cNo lore");
                        return true;
                    } else {
                        im.setLore(null);
                        sender.sendMessage("§fCleared lore");
                    }
                } else {
                    sender.sendMessage("§cUsage: " + cmd.getUsage());
                    return true;
                }
            } else {
                sender.sendMessage("§cYou are not holding anything");
                return true;
            }
            item.setItemMeta(im);
            return true;
        }
        sender.sendMessage("§cYou need to be a player to run this command");
        return true;
    }
}
