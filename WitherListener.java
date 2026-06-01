package com.smp.voidheartboss;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ElderGuardianListener implements Listener {

    private final VoidHeartBoss plugin;
    private final Map<UUID, BossBar> bossBars = new HashMap<>();
    private static final double REWARD_RADIUS = 300.0;

    public ElderGuardianListener(VoidHeartBoss plugin) {
        this.plugin = plugin;
        startBossBarUpdater();
    }

    @EventHandler
    public void onElderGuardianSpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof ElderGuardian elder)) return;
        BossBar bar = Bukkit.createBossBar(
            "\u00a7b\u00a7l\u2693 Gardien des Profondeurs \u2693",
            BarColor.RED,
            BarStyle.SOLID
        );
        bar.setVisible(true);
        bossBars.put(elder.getUniqueId(), bar);
    }

    private void startBossBarUpdater() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, BossBar> entry : new HashMap<>(bossBars).entrySet()) {
                    Entity entity = Bukkit.getEntity(entry.getKey());
                    if (!(entity instanceof ElderGuardian elder)) {
                        entry.getValue().removeAll();
                        bossBars.remove(entry.getKey());
                        continue;
                    }
                    BossBar bar = entry.getValue();
                    double progress = elder.getHealth() / elder.getMaxHealth();
                    bar.setProgress(Math.max(0, Math.min(1, progress)));

                    // Ajoute les joueurs proches a la bossbar
                    for (Player p : elder.getWorld().getPlayers()) {
                        if (p.getLocation().distance(elder.getLocation()) <= 100) {
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
    public void onElderGuardianDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof ElderGuardian elder)) return;

        BossBar bar = bossBars.remove(elder.getUniqueId());
        if (bar != null) bar.removeAll();

        Location deathLoc = elder.getLocation();
        World world = deathLoc.getWorld();

        // Drop la Larme de l'Ancien
        world.dropItemNaturally(deathLoc, CustomItems.createLarmeAncien());

        // Joueurs dans 300 blocs
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
        Bukkit.broadcastMessage("\u00a7b\u00a7l\u00a7k|\u00a7r \u00a73\u00a7l\u2693 LE GARDIEN DES PROFONDEURS EST VAINCU \u2693 \u00a7b\u00a7l\u00a7k|");
        Bukkit.broadcastMessage("\u00a77Les oceans pleurent leur gardien...");
        Bukkit.broadcastMessage("");

        new BukkitRunnable() {
            int ticks = 0;
            double angle = 0;

            @Override
            public void run() {
                ticks++;
                angle += 6;

                // Spirale de particules aqua
                for (int i = 0; i < 4; i++) {
                    double rad = Math.toRadians(angle + (i * 90));
                    double height = (ticks / 8.0) % 12;
                    double x = Math.cos(rad) * 4;
                    double z = Math.sin(rad) * 4;
                    world.spawnParticle(Particle.FALLING_WATER, loc.clone().add(x, height, z), 3, 0.1, 0.1, 0.1, 0);
                    world.spawnParticle(Particle.END_ROD, loc.clone().add(x, height, z), 1, 0.1, 0.1, 0.1, 0.01);
                }

                // Explosion d'eau toutes les 25 ticks
                if (ticks % 25 == 0) {
                    world.spawnParticle(Particle.SPLASH, loc.clone().add(0, 2, 0), 100, 3, 3, 3, 0.5);
                    world.spawnParticle(Particle.BUBBLE_POP, loc.clone().add(0, 2, 0), 50, 2, 2, 2, 0.1);
                    world.playSound(loc, Sound.ENTITY_ELDER_GUARDIAN_DEATH, 2.0f, 0.8f);
                }

                // Eclairs toutes les 40 ticks
                if (ticks % 40 == 0) {
                    world.strikeLightningEffect(loc.clone().add(
                        (Math.random() - 0.5) * 8, 0, (Math.random() - 0.5) * 8
                    ));
                }

                // Particules aqua autour des joueurs
                for (Player p : players) {
                    for (int i = 0; i < 5; i++) {
                        double rad = Math.toRadians(angle * 3 + (i * 72));
                        double px = Math.cos(rad) * 1.5;
                        double pz = Math.sin(rad) * 1.5;
                        p.getWorld().spawnParticle(Particle.FALLING_WATER,
                            p.getLocation().clone().add(px, 1, pz), 1, 0, 0, 0, 0);
                    }
                }

                if (ticks % 20 == 0) {
                    world.playSound(loc, Sound.AMBIENT_UNDERWATER_LOOP, 1.0f, 1.0f);
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
        Bukkit.broadcastMessage("\u00a7b\u00a7l================================================");
        Bukkit.broadcastMessage("\u00a73\u00a7l         \u2693 L'EVEILLE EST NE \u2693");
        Bukkit.broadcastMessage("\u00a77   Les profondeurs ont juge dignes :");
        Bukkit.broadcastMessage("");

        for (Player p : players) {
            Bukkit.broadcastMessage("\u00a7b    \u2726 \u00a7l" + p.getName() + "\u00a7r\u00a7b \u2726");
        }

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("\u00a77   Ils portent desormais le titre de");
        Bukkit.broadcastMessage("\u00a7e\u00a7l         L'EVEILLE");
        Bukkit.broadcastMessage("\u00a7b\u00a7l================================================");
        Bukkit.broadcastMessage("");

        for (Player p : players) {
            // Effets visuels
            p.getWorld().spawnParticle(Particle.SPLASH, p.getLocation().clone().add(0, 1, 0), 100, 1, 1, 1, 0.3);
            p.getWorld().spawnParticle(Particle.END_ROD, p.getLocation().clone().add(0, 1, 0), 50, 0.5, 1, 0.5, 0.05);
            p.getWorld().strikeLightningEffect(p.getLocation());
            p.getWorld().playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 2.0f, 1.0f);

            if (!plugin.hasElderReward(p.getUniqueId())) {
                plugin.markElderRewarded(p.getUniqueId());

                // +2 coeurs dorés (Absorption)
                p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, Integer.MAX_VALUE, 0, false, false, true));

                // Augmente vie max de 4 (2 coeurs)
                AttributeInstance maxHealth = p.getAttribute(Attribute.MAX_HEALTH);
                if (maxHealth != null) {
                    maxHealth.setBaseValue(maxHealth.getBaseValue() + 4.0);
                    p.setHealth(Math.min(p.getHealth() + 4.0, maxHealth.getBaseValue()));
                }

                // Respiration infinie
                p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0, false, false, false));

                // Message personnel
                p.sendMessage("");
                p.sendMessage("\u00a7b\u00a7l================================================");
                p.sendMessage("\u00a73\u00a7l    \u2726 VOUS ETES DESORMAIS L'EVEILLE \u2726");
                p.sendMessage("\u00a77Les profondeurs vous ont juge digne.");
                p.sendMessage("\u00a77Vous ressentez le pouvoir de l'ocean");
                p.sendMessage("\u00a77couler en vous...");
                p.sendMessage("");
                p.sendMessage("\u00a77Vous avez recu :");
                p.sendMessage("\u00a7e  \u25b8 +2 coeurs dores permanents");
                p.sendMessage("\u00a7b  \u25b8 Aura aqua permanente");
                p.sendMessage("\u00a7b  \u25b8 Respiration infinie permanente");
                p.sendMessage("\u00a7b\u00a7l================================================");
                p.sendMessage("");

                // Titre a l'ecran
                p.sendTitle("\u00a7e\u00a7lL'EVEILLE", "\u00a77Les profondeurs vous saluent...", 20, 80, 20);
            } else {
                p.sendMessage("\u00a7bVous avez deja les recompenses de L'Eveille !");
            }
        }
    }
}
