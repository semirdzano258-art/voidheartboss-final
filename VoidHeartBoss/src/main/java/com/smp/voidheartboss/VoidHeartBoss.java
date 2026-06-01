package com.smp.voidheartboss;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VoidHeartBoss extends JavaPlugin {

    private static VoidHeartBoss instance;
    private final Set<UUID> rewardedElder = new HashSet<>();
    private final Set<UUID> rewardedWither = new HashSet<>();
    private final Set<UUID> firstJoinPlayers = new HashSet<>();

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        loadData();

        getServer().getPluginManager().registerEvents(new ElderGuardianListener(this), this);
        getServer().getPluginManager().registerEvents(new WitherListener(this), this);
        getServer().getPluginManager().registerEvents(new PortalListener(this), this);
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new WitherSkeletonListener(this), this);
        getServer().getPluginManager().registerEvents(new AuraListener(this), this);

        getLogger().info("VoidHeartBoss active ! Que le vide guide les aventuriers...");
    }

    @Override
    public void onDisable() {
        saveData();
        getLogger().info("VoidHeartBoss desactive.");
    }

    public static VoidHeartBoss getInstance() { return instance; }

    public boolean hasElderReward(UUID uuid) { return rewardedElder.contains(uuid); }
    public boolean hasWitherReward(UUID uuid) { return rewardedWither.contains(uuid); }
    public boolean hasJoined(UUID uuid) { return firstJoinPlayers.contains(uuid); }

    public void markElderRewarded(UUID uuid) { rewardedElder.add(uuid); saveData(); }
    public void markWitherRewarded(UUID uuid) { rewardedWither.add(uuid); saveData(); }
    public void markJoined(UUID uuid) { firstJoinPlayers.add(uuid); saveData(); }

    private void saveData() {
        java.util.List<String> elder = new java.util.ArrayList<>();
        for (UUID u : rewardedElder) elder.add(u.toString());
        getConfig().set("rewarded_elder", elder);

        java.util.List<String> wither = new java.util.ArrayList<>();
        for (UUID u : rewardedWither) wither.add(u.toString());
        getConfig().set("rewarded_wither", wither);

        java.util.List<String> joins = new java.util.ArrayList<>();
        for (UUID u : firstJoinPlayers) joins.add(u.toString());
        getConfig().set("first_join", joins);

        saveConfig();
    }

    private void loadData() {
        for (String s : getConfig().getStringList("rewarded_elder")) {
            try { rewardedElder.add(UUID.fromString(s)); } catch (Exception ignored) {}
        }
        for (String s : getConfig().getStringList("rewarded_wither")) {
            try { rewardedWither.add(UUID.fromString(s)); } catch (Exception ignored) {}
        }
        for (String s : getConfig().getStringList("first_join")) {
            try { firstJoinPlayers.add(UUID.fromString(s)); } catch (Exception ignored) {}
        }
        getLogger().info("Donnees chargees avec succes.");
    }
}
