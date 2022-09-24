package tech.mekb.survivalextras;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class SurvivalExtras extends JavaPlugin {
    public static FileConfiguration config;
    @Override
    public void onEnable() {
        MOTDListener  motdListener = new MOTDListener();
        config = getConfig();
        config.addDefault("motd", new String[] {
                "§bHello, %player%",
                "§e§oWelcome",
                "§e§oWelcome %player%",
                "§6public static void §emain§r(String[] §eargs§r) { }",
                "§6public static void §emain§r(String[] §eargs§r) { }",
                "§6public static void §emain§r(String[] §eargs§r) { }",
                "§6public static void §emain§r(String[] §eargs§r) { }",
                "§6public static void §emain§r(String[] §eargs§r) { }",
                "System.§dout§r.println(§2\"%player%\"§r)§6;",
                "System.§dout§r.println(§2\"Hello, World! %player%\"§r)§6;",
                "System.§dout§r.println(§2\"Hello, World!\"§r)§6;",
                "§c§lTEXTREME§r is the best text editor!",
				"§e§oED IS THE STANDARD EDITOR!!!",
				"§bmekbase§r is better than any other distro",
				"§aodus§r > §csudo§r",
				"§eAlphys is better than ASCII",
				"§fYou are filled with §cDETERGENT§f.",
				"§f%player% is filled with §cDETERGENT§f."
        });
        config.addDefault("signEdit", true);
        config.addDefault("infiniteAnvil", true);
        config.addDefault("autoReplant", true);
        this.getCommand("rename").setExecutor(new RenameCommand());
        this.getCommand("lore").setExecutor(new LoreCommand());
        config.options().copyDefaults(true);
        saveConfig();
		reloadConfig();
		config = getConfig();
        motdListener.motd = config.getStringList("motd");
        if (config.getBoolean("signEdit")) {
            getServer().getPluginManager().registerEvents(new SignListener(), this);
        }
        for (String j : motdListener.motd) {
            if (!j.contains("%player%")) {
                motdListener.motdNoPlayer.add(j);
            }
        }
        getServer().getPluginManager().registerEvents(motdListener, this);
        if (config.getBoolean("infiniteAnvil")) {
            getServer().getPluginManager().registerEvents(new AnvilListener(), this);
        }
        if (config.getBoolean("autoReplant")) {
            getServer().getPluginManager().registerEvents(new BreakListener(this), this);
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
                .replaceAll("(?<!&)&([klmnorx0-9a-f])", "§$1")
                .replaceAll("(?<=&)&([klmnorx0-9a-f])", "$1");
    }
}
