package tech.mekb.survivalextras;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MOTDListener implements Listener {
    private final HashMap<InetAddress, String> playerIPs = new HashMap<>();
    Random rand = new Random();
    public List<String> motdNoPlayer = new ArrayList<>();
    public List<String> motd;

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        InetSocketAddress addr_ = p.getAddress();
        if (addr_ == null) return;
        InetAddress addr = addr_.getAddress();
        playerIPs.put(addr, p.getName());
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
