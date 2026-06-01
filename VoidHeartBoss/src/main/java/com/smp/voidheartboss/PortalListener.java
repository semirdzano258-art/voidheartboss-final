package com.smp.voidheartboss;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

public class PortalListener implements Listener {

    private final VoidHeartBoss plugin;

    public PortalListener(VoidHeartBoss plugin) {
        this.plugin = plugin;
    }

    // Bloque l'allumage du portail Nether
    @EventHandler
    public void onPortalIgnite(BlockIgniteEvent event) {
        Block block = event.getBlock();
        if (event.getPlayer() == null) return;
        Player player = event.getPlayer();
        if (player.isOp()) return;
        if (!isNetherPortalFrame(block)) return;

        if (!hasLarmeAncien(player)) {
            event.setCancelled(true);
            sendNetherBlocked(player);
        }
    }

    // Bloque le passage dans les portails
    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        if (player.isOp()) return;
        if (event.getTo() == null) return;

        // Portail Nether
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL
            && event.getTo().getWorld().getEnvironment() == org.bukkit.World.Environment.NETHER) {
            if (!hasLarmeAncien(player)) {
                event.setCancelled(true);
                sendNetherBlocked(player);
            } else {
                sendNetherAllowed(player);
            }
        }

        // Portail End
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL
            && event.getTo().getWorld().getEnvironment() == org.bukkit.World.Environment.THE_END) {
            if (!hasEssenceNeant(player)) {
                event.setCancelled(true);
                sendEndBlocked(player);
            } else {
                sendEndAllowed(player);
            }
        }
    }

    private void sendNetherBlocked(Player player) {
        player.sendMessage("\u00a74\u00a7l>> ACCES REFUSE <<\u00a7r");
        player.sendMessage("\u00a7cCet environnement est d'une dangerosite extreme.");
        player.sendMessage("\u00a77Revenez avec la \u00a7b\u00a7lLarme de l'Ancien");
        player.sendMessage("\u00a77pour franchir cette barriere.");
    }

    private void sendNetherAllowed(Player player) {
        player.sendMessage("\u00a76\u00a7l>> Porte des Enfers <<\u00a7r");
        player.sendMessage("\u00a77Vous sentez une chaleur intense vous envahir...");
        player.sendMessage("\u00a7cL'au-dela vous attend. \u00a7eBonne chance, aventurier.");
    }

    private void sendEndBlocked(Player player) {
        player.sendMessage("\u00a75\u00a7l>> ACCES REFUSE <<\u00a7r");
        player.sendMessage("\u00a7dLe vide vous repousse...");
        player.sendMessage("\u00a77Revenez avec l'\u00a75\u00a7lEssence du Neant");
        player.sendMessage("\u00a77pour franchir cette barriere.");
    }

    private void sendEndAllowed(Player player) {
        player.sendMessage("\u00a75\u00a7l>> Portail du Neant <<\u00a7r");
        player.sendMessage("\u00a77Le vide vous appelle...");
        player.sendMessage("\u00a7dQue votre volonte soit plus forte que les tenebres.");
    }

    private boolean hasLarmeAncien(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (CustomItems.isLarmeAncien(item)) return true;
        }
        return CustomItems.isLarmeAncien(player.getInventory().getItemInOffHand());
    }

    private boolean hasEssenceNeant(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (CustomItems.isEssenceNeant(item)) return true;
        }
        return CustomItems.isEssenceNeant(player.getInventory().getItemInOffHand());
    }

    private boolean isNetherPortalFrame(Block block) {
        BlockFace[] faces = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};
        for (BlockFace face : faces) {
            if (block.getRelative(face).getType() == Material.OBSIDIAN) return true;
        }
        return false;
    }
}
