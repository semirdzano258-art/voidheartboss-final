package com.smp.voidheartboss;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class WitherListener implements Listener {

    private final VoidHeartBoss plugin;
    private final Map<UUID, BossBar> bossBars = new HashMap<>();
    private static final double REWARD_RADIUS = 300.0;

    public WitherListener(VoidHeartBoss plugin) {
        this.plugin = plugin;
        startBossBarUpdater();
    }

    @EventHandler
    public void onWitherSpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof Wither wither)) return;
        BossBar bar = Bukkit.createBossBar(
            "\u00a74\u00a7l\u2620 Le Wither \u2620",
            BarColor.RED,
            BarStyle.SOLID
        );
        bar.setVisible(true);
        bossBars.put(wither.getUniqueId(), bar);
    }

    private void startBossBarUpdater() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, BossBar> entry : new HashMap<>(bossBars).entrySet()) {
                    Entity entity = Bukkit.getEntity(entry.getKey());
                    if (!(entity instanceof Wither wither)) {
                        entry.getValue().removeAll();
                        bossBars.remove(entry.getKey());
                        continue;
                    }
                    BossBar bar = entry.getValue();
                    double progress = wither.getHealth() / wither.getMaxHealth();
                    bar.setProgress(Math.max(0, Math.min(1, progress)));

                    for (Player p : wither.getWorld().getPlayers()) {
                        if (p.getLocation().distance(wither.getLocation()) <= 150) {
                            if (!bar.getPlayers().contains(p)) bar.addPlayer(p);
                        } else {
                            bar.removePlayer(p);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 10L);
    }

    @EventHandler
    public void onWitherDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Wither wither)) return;

        BossBar bar = bossBars.remove(wither.getUniqueId());
        if (bar != null) bar.removeAll();

        Location deathLoc = wither.getLocation();
        World world = deathLoc.getWorld();

        // Drop Essence du Neant (en plus de la Nether Star vanilla)
        world.dropItemNaturally(deathLoc, CustomItems.createEssenceNeant());

        List<Player> nearbyPlayers = new ArrayList<>();
        for (Player p : world.getPlayers()) {
            if (p.getLocation().distance(deathLoc) <= REWARD_RADIUS) {
                nearbyPlayers.add(p);
            }
        }

        startAnimation(deathLoc, world, nearbyPlayers);
    }

    private void startAnimation(Location loc, World world, List<Player> players) {
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("\u00a74\u00a7l\u00a7k|\u00a7r \u00a7c\u00a7l\u2620 LE WITHER EST VAINCU \u2620 \u00a74\u00a7l\u00a7k|");
        Bukkit.broadcastMessage("\u00a77Le neant tremble sous la defaite de son champion...");
        Bukkit.broadcastMessage("");

        new BukkitRunnable() {
            int ticks = 0;
            double angle = 0;

            @Override
            public void run() {
                ticks++;
                angle += 8;

                // Spirale de particules rouges/noires
                for (int i = 0; i < 3; i++) {
                    double rad = Math.toRadians(angle + (i * 120));
                    double height = (ticks / 10.0) % 15;
                    double x = Math.cos(rad) * 5;
                    double z = Math.sin(rad) * 5;
                    world.spawnParticle(Particle.SMOKE, loc.clone().add(x, height, z), 5, 0.1, 0.1, 0.1, 0.02);
                    world.spawnParticle(Particle.SOUL_FIRE_FLAME, loc.clone().add(x, height, z), 2, 0.1, 0.1, 0.1, 0.01);
                }

                // Explosion de particules toutes les 20 ticks
                if (ticks % 20 == 0) {
                    world.spawnParticle(Particle.EXPLOSION, loc.clone().add(0, 3, 0), 5, 3, 3, 3, 0);
                    world.spawnParticle(Particle.SOUL, loc, 50, 5, 5, 5, 0.3);
                    world.playSound(loc, Sound.ENTITY_WITHER_DEATH, 2.0f, 0.8f);
                }

                // Eclairs toutes les 30 ticks
                if (ticks % 30 == 0) {
                    world.strikeLightningEffect(loc.clone().add(
                        (Math.random() - 0.5) * 10, 0, (Math.random() - 0.5) * 10
                    ));
                    world.playSound(loc, Sound.ENTITY_WITHER_AMBIENT, 1.0f, 0.5f);
                }

                // Particules rouges autour des joueurs
                for (Player p : players) {
                    for (int i = 0; i < 4; i++) {
                        double rad = Math.toRadians(angle * 2 + (i * 90));
                        double px = Math.cos(rad) * 1.5;
                        double pz = Math.sin(rad) * 1.5;
                        p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME,
                            p.getLocation().clone().add(px, 1, pz), 1, 0, 0, 0, 0);
                    }
                }

                if (ticks >= 300) {
                    cancel();
                    giveRewards(loc, world, players);
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    private void giveRewards(Location loc, World world, List<Player> players) {
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("\u00a74\u00a7l================================================");
        Bukkit.broadcastMessage("\u00a7c\u00a7l         \u2620 L'OBSCUR EST NE \u2620");
        Bukkit.broadcastMessage("\u00a77   Le neant a marque pour l'eternite :");
        Bukkit.broadcastMessage("");

        for (Player p : players) {
            Bukkit.broadcastMessage("\u00a7c    \u2620 \u00a7l" + p.getName() + "\u00a7r\u00a7c \u2620");
        }

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("\u00a77   Ils portent desormais le titre de");
        Bukkit.broadcastMessage("\u00a7c\u00a7l         L'OBSCUR");
        Bukkit.broadcastMessage("\u00a74\u00a7l================================================");
        Bukkit.broadcastMessage("");

        for (Player p : players) {
            p.getWorld().spawnParticle(Particle.SOUL, p.getLocation().clone().add(0, 1, 0), 100, 1, 1, 1, 0.1);
            p.getWorld().spawnParticle(Particle.SMOKE, p.getLocation().clone().add(0, 1, 0), 50, 0.5, 1, 0.5, 0.05);
            p.getWorld().strikeLightningEffect(p.getLocation());
            p.getWorld().playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 2.0f, 0.7f);
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0f, 1.5f);

            if (!plugin.hasWitherReward(p.getUniqueId())) {
                plugin.markWitherRewarded(p.getUniqueId());

                // +2 coeurs rouges permanents
                AttributeInstance maxHealth = p.getAttribute(Attribute.MAX_HEALTH);
                if (maxHealth != null) {
                    maxHealth.setBaseValue(maxHealth.getBaseValue() + 4.0);
                    p.setHealth(Math.min(p.getHealth() + 4.0, maxHealth.getBaseValue()));
                }

                // Immunite au feu permanente
                p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false, false));

                // Message personnel
                p.sendMessage("");
                p.sendMessage("\u00a74\u00a7l================================================");
                p.sendMessage("\u00a7c\u00a7l    \u2620 VOUS ETES DESORMAIS L'OBSCUR \u2620");
                p.sendMessage("\u00a77Le neant vous a marque pour l'eternite.");
                p.sendMessage("\u00a77Aucune flamme ne pourra plus");
                p.sendMessage("\u00a77vous consumer...");
                p.sendMessage("");
                p.sendMessage("\u00a77Vous avez recu :");
                p.sendMessage("\u00a7c  \u25b8 +2 coeurs rouges permanents");
                p.sendMessage("\u00a7c  \u25b8 Aura rouge/noire permanente");
                p.sendMessage("\u00a7c  \u25b8 Immunite au feu permanente");
                p.sendMessage("\u00a74\u00a7l================================================");
                p.sendMessage("");

                p.sendTitle("\u00a7c\u00a7lL'OBSCUR", "\u00a77Le neant vous appartient...", 20, 80, 20);
            } else {
                p.sendMessage("\u00a7cVous avez deja les recompenses de L'Obscur !");
            }
        }
    }
}
