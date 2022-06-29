package luxielx;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener {

    public static Main main;
    public static FileConfiguration config;
    public static ArrayList<UUID> disableHD = new ArrayList<>() ;
    public static Main getPlugin() {
        return main;
    }

    public static String getConfigPath(String path) {
        return config.getString(path);
    }

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
        new PlaceHolder(this).register();
        } else {
            throw new RuntimeException("Could not find PlaceholderAPI!! Plugin can not work without it!");
        }
        main = this;
        regLis();
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        reloadConfig();
        config = this.getConfig();
    }

    private void regLis() {
        this.getServer().getPluginManager().registerEvents(new Lis(), this);
    }

    public void onDisable() {
    }

    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        try {
            if (arg.equalsIgnoreCase("di")) {
                if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("di.admin")) {
                    sender.sendMessage(ChatColor.RED + "[HypixelDamaceIndicator] Config Reloaded");
                    reloadConfig();
                    config = this.getConfig();
                }
                if (args[0].equalsIgnoreCase("toggle") && sender instanceof Player) {
                    Player p = (Player) sender;
                    setToggle(p, isDisabled(p));
                    if (isDisabled(p)) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("disable")));
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("enable")));

                    }

                }
                return false;
            }
        }catch (Exception e){

        }
        return true;
    }

    public static void setToggle(Player p,boolean boo){
        if(boo){
            if(disableHD.contains(p.getUniqueId())) disableHD.remove(p.getUniqueId());
        }else{
            disableHD.add(p.getUniqueId());
        }
    }

    public static boolean isDisabled(Player p){
        if(disableHD.contains(p.getUniqueId())){
            return true;
        }
        return false;
    }
}

