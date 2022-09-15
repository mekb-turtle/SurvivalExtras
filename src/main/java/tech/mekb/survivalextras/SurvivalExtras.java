package tech.mekb.survivalextras;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public final class SurvivalExtras extends JavaPlugin implements Listener {
    FileConfiguration config;
    List<String> motdNoPlayer = new ArrayList<>();
    List<String> motd;
    private final HashMap<InetAddress, String> playerIPs = new HashMap<>();
    Random rand = new Random();
    boolean signEdit;
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        config = getConfig();
        config.addDefault("motd", new String[] {
                "§e%player% is a nerd",
                "§bHello, %player%",
                "§e§oWelcome",
                "§e§oWelcome %player%",
                "§6public static void §emain§r(String[] §eargs§r) { }",
                "System.§dout§r.println(§2\"%player%\"§r)§6;",
                "§c§lTEXTREME§r is the best text editor!"
        });
        this.getCommand("rename").setExecutor(new RenameCommand());
        this.getCommand("lore").setExecutor(new LoreCommand());
        config.addDefault("signEdit", true);
        config.options().copyDefaults(true);
        saveConfig();
        motd = config.getStringList("motd");
        signEdit = config.getBoolean("signEdit");
        for (String j : motd) {
            if (!j.contains("%player%")) {
                motdNoPlayer.add(j);
            }
        }
    }

    public static String getArgString(String[] arg, int startIndex) {
        StringBuilder str = new StringBuilder();
        for (int i = startIndex; i < arg.length; ++i) {
            if (i > startIndex) str.append(" ");
            str.append(arg[i]);
        }
        return str.toString();
    }

    public static String replaceColours(String str) {
        return str
                .replaceAll("(?<!&)&([klmnox0-9a-f])", "§$1")
                .replaceAll("(?<=&)&([klmnox0-9a-f])", "$1");
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        InetSocketAddress addr_ = p.getAddress();
        if (addr_ == null) return;
        InetAddress addr = addr_.getAddress();
        playerIPs.put(addr, p.getName());
    }

    @EventHandler
    public void use(PlayerInteractEvent e) {
        if (!signEdit) return;
        Block b = e.getClickedBlock();
        if (b == null) return;
        Material m = b.getType();
        if (m == Material.ACACIA_SIGN ||
                m == Material.BIRCH_SIGN ||
                m == Material.CRIMSON_SIGN ||
                m == Material.DARK_OAK_SIGN ||
                m == Material.JUNGLE_SIGN ||
                m == Material.MANGROVE_SIGN ||
                m == Material.OAK_SIGN ||
                m == Material.SPRUCE_SIGN ||
                m == Material.WARPED_SIGN ||
                m == Material.ACACIA_WALL_SIGN ||
                m == Material.BIRCH_WALL_SIGN ||
                m == Material.CRIMSON_WALL_SIGN ||
                m == Material.DARK_OAK_WALL_SIGN ||
                m == Material.JUNGLE_WALL_SIGN ||
                m == Material.MANGROVE_WALL_SIGN ||
                m == Material.OAK_WALL_SIGN ||
                m == Material.SPRUCE_WALL_SIGN ||
                m == Material.WARPED_WALL_SIGN) {
            e.getPlayer().openSign((Sign) b);
        }
    }

    @EventHandler
    public void motd(ServerListPingEvent e) {
        InetAddress addr = e.getAddress();
        if (playerIPs.containsKey(addr)) {
            String u = playerIPs.get(addr);
            if (u != null) {
                e.setMotd(motd.get(rand.nextInt(motd.size()))
                        .replaceAll("%player%", u));
                return;
            }
        }
        e.setMotd(motdNoPlayer.get(rand.nextInt(motdNoPlayer.size())));
    }
}
