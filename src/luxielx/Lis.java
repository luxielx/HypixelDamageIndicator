package luxielx;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Lis implements Listener {


    public static String randomChatColor() {
        String s = "6,e,c,b,d";
        return "§" + s.split(",")[randomInt(0, s.split(",").length - 1)];
    }

    public static int randomInt(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    @EventHandler
    public void onHit(EntityDamageEvent e) {
        if (e.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) e.getEntity();
            Hologram hologram = HologramsAPI.createHologram(Main.getPlugin(), entity.getEyeLocation().clone().add(
                    ThreadLocalRandom.current().nextDouble(-0.5, 0.5),
                    ThreadLocalRandom.current().nextDouble(-0.5, 0.5),
                    ThreadLocalRandom.current().nextDouble(-0.5, 0.5)
            ));
            VisibilityManager visiblityManager = hologram.getVisibilityManager();
            visiblityManager.setVisibleByDefault(false);
            String indicator = "";
            int damage = (int) Math.round(e.getDamage());
            if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
                indicator = convertConfig(Main.getConfigPath("sweepattack"), damage);
            } else if (e.getCause() == EntityDamageEvent.DamageCause.MAGIC) {
                indicator = convertConfig(Main.getConfigPath("magic"), damage);
            } else if (e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                indicator = convertConfig(Main.getConfigPath("fire"), damage);
            } else if (e.getCause() == EntityDamageEvent.DamageCause.POISON) {
                indicator = convertConfig(Main.getConfigPath("poison"), damage);
            } else if (e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                indicator = convertConfig(Main.getConfigPath("projectile"), damage);
            } else if (e.getCause() == EntityDamageEvent.DamageCause.THORNS) {
                indicator = convertConfig(Main.getConfigPath("thorns"), damage);
            }
            for (Entity en : entity.getNearbyEntities(50, 50, 50)) {
                if (en instanceof Player) {
                    Player p = (Player) en;
                    if (!Main.isDisabled(p)) {
                        visiblityManager.showTo(p);
                    }
                }
            }
            hologram.appendTextLine(indicator);
            Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
                hologram.delete();
            }, Main.config.getInt("fadedouttime") * 20);
        }
    }

    @EventHandler
    public void onHit2(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            if (e.getEntity() instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity) e.getEntity();
                Hologram hologram = HologramsAPI.createHologram(Main.getPlugin(), entity.getEyeLocation().clone().add(
                        ThreadLocalRandom.current().nextDouble(-0.5, 0.5),
                        ThreadLocalRandom.current().nextDouble(-0.5, 0.5),
                        ThreadLocalRandom.current().nextDouble(-0.5, 0.5)
                ));
                VisibilityManager visiblityManager = hologram.getVisibilityManager();
                visiblityManager.setVisibleByDefault(false);
                String indicator = "";
                int damage = (int) Math.round(e.getDamage());
                boolean flag = p.getFallDistance() > 0.0F && !p.isOnGround() && !p.hasPotionEffect(PotionEffectType.BLINDNESS) && p.getVehicle() == null;
                if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    if (flag) {
                        String a = damage +"";
                        String[] az = a.split("");
                        String cb = "";
                        for (String z : az) {
                            cb += randomChatColor() + z;
                        }
                        indicator = convertConfig(Main.getConfigPath("critattack"), cb);
                    } else {
                        indicator = convertConfig(Main.getConfigPath("attack"), damage);
                    }
                }
                for (Entity en : entity.getNearbyEntities(50, 50, 50)) {
                    if (en instanceof Player) {
                        Player pa = (Player) en;
                        if (!Main.isDisabled(pa)) {
                            visiblityManager.showTo(pa);
                        }
                    }
                }



                hologram.appendTextLine(indicator);
                Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
                    hologram.delete();
                }, Main.config.getInt("fadedouttime") * 20);
            }
        }
    }

    public String convertConfig(String str, int damage) {
        return ChatColor.translateAlternateColorCodes('&', str.replace("%damage%", damage + ""));
    }
    public String convertConfig(String str, String damage) {
        return ChatColor.translateAlternateColorCodes('&', str.replace("%damage%", damage + ""));
    }
}
