package com.smp.voidheartboss;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class AuraListener implements Listener {

    private final VoidHeartBoss plugin;

    public AuraListener(VoidHeartBoss plugin) {
        this.plugin = plugin;
        startAuraTask();
    }

    private void startAuraTask() {
        new BukkitRunnable() {
            double angle = 0;

            @Override
            public void run() {
                angle += 12;

                for (Player p : Bukkit.getOnlinePlayers()) {
                    boolean hasElder = plugin.hasElderReward(p.getUniqueId());
                    boolean hasWither = plugin.hasWitherReward(p.getUniqueId());

                    if (!hasElder && !hasWither) continue;

                    // Aura aqua (Elder Guardian - L'Eveille)
                    if (hasElder) {
                        for (int i = 0; i < 4; i++) {
                            double rad = Math.toRadians(angle + (i * 90));
                            double x = Math.cos(rad) * 1.2;
                            double z = Math.sin(rad) * 1.2;
                            p.getWorld().spawnParticle(
                                Particle.FALLING_WATER,
                                p.getLocation().clone().add(x, 0.8, z),
                                1, 0, 0, 0, 0
                            );
                        }
                        // Particule au dessus de la tete
                        p.getWorld().spawnParticle(
                            Particle.END_ROD,
                            p.getLocation().clone().add(0, 2.3, 0),
                            1, 0.1, 0, 0.1, 0.01
                        );
                    }

                    // Aura rouge/noire (Wither - L'Obscur)
                    if (hasWither) {
                        for (int i = 0; i < 4; i++) {
                            double rad = Math.toRadians(-angle + (i * 90));
                            double x = Math.cos(rad) * 1.0;
                            double z = Math.sin(rad) * 1.0;
                            p.getWorld().spawnParticle(
                                Particle.SOUL_FIRE_FLAME,
                                p.getLocation().clone().add(x, 0.3, z),
                                1, 0, 0, 0, 0
                            );
                        }
                        p.getWorld().spawnParticle(
                            Particle.SMOKE,
                            p.getLocation().clone().add(0, 0.1, 0),
                            2, 0.3, 0, 0.3, 0.01
                        );
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 3L);
    }
}
