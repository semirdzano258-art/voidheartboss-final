package com.smp.voidheartboss;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class JoinListener implements Listener {

    private final VoidHeartBoss plugin;

    public JoinListener(VoidHeartBoss plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        updatePlayerTitle(player);

        if (!plugin.hasJoined(player.getUniqueId())) {
            plugin.markJoined(player.getUniqueId());
            new BukkitRunnable() {
                @Override
                public void run() {
                    sendWelcomeMessage(player);
                    giveWelcomeBook(player);
                    player.sendTitle(
                        "\u00a78\u00a7lERRANT DES LIMBES",
                        "\u00a77Bienvenue sur VoidHeart...",
                        20, 100, 20
                    );
                }
            }.runTaskLater(plugin, 40L);
        }
    }

    public void updatePlayerTitle(Player player) {
        org.bukkit.scoreboard.Scoreboard board = player.getScoreboard();
        if (board == null) board = Bukkit.getScoreboardManager().getMainScoreboard();

        String teamName = "vh_" + player.getName().substring(0, Math.min(player.getName().length(), 10));
        org.bukkit.scoreboard.Team team = board.getTeam(teamName);
        if (team == null) team = board.registerNewTeam(teamName);

        String prefix;
        if (plugin.hasWitherReward(player.getUniqueId())) {
            prefix = "\u00a74[\u00a7cL'Obscur\u00a74] ";
        } else if (plugin.hasElderReward(player.getUniqueId())) {
            prefix = "\u00a76[\u00a7eL'Eveille\u00a76] ";
        } else {
            prefix = "\u00a78[\u00a75Errant\u00a78] ";
        }

        team.setPrefix(prefix);
        team.addEntry(player.getName());
        player.setScoreboard(board);
    }

    private void giveWelcomeBook(Player player) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setTitle("\u00a75\u00a7lVoidHeart");
        meta.setAuthor("Le Vide");

        meta.addPage("\u00a75\u00a7l* VOIDHEART *\n\nTu foules pour la\npremiere fois ces\nterres anciennes...\n\nLe monde est vaste,\ndangereux, et\nimpitoyable.\n\n\u00a75Bonne chance,\naventurier.");
        meta.addPage("\u00a74\u00a7l[LA MORT]\n\nTa tete drop a ta\nmort. Tes amis ont\n\u00a7c\u00a7l24h\u00a70 pour la\nramasser.\n\nPosez la sur un\nBloc d'Obsidienne\navec les ressources\nrequises.\n\n\u00a74Les ressources\naugmentent a\nchaque mort...");
        meta.addPage("\u00a75\u00a7l[LES PORTES]\n\nLes portails ne\ns'ouvrent qu'a\nceux qui en sont\ndignes...\n\nNether : Requiert\nune relique des\nprofondeurs.\n\nEnd : Requiert un\nfragment de\nl'obscurite.");
        meta.addPage("\u00a76\u00a7l[LES EPREUVES]\n\n\u00a7bElder Guardian\nGardien des abysses\n-> Debloque le Nether\n\n\u00a7cWither\nChampion du neant\n-> Debloque l'End\n\n\u00a75Ender Dragon\nSeigneur du vide\n-> La transcendance");
        meta.addPage("\u00a75\u00a7l[PROGRESSION]\n\n\u00a78Errant des Limbes\n(Depart)\n\n\u00a7eL'Eveille\nElder Guardian\n\n\u00a7cL'Obscur\nWither\n\n\u00a7dL'Ascendant\nEnder Dragon\n\n\u00a75Chaque victoire\nte forge...");
        meta.addPage("\u00a76\u00a7l[RECOMPENSES]\n\n\u00a7bL'Eveille\n+2 coeurs dores\nRespiration infinie\n\n\u00a7cL'Obscur\n+2 coeurs rouges\nImmunite au feu\n\n\u00a7dL'Ascendant\n+2 coeurs violets\nImmunite aux chutes\nOeuf de dragon");

        book.setItemMeta(meta);
        player.getInventory().addItem(book);
        player.sendMessage("\u00a75\u00a7l[VoidHeart] \u00a77Un livre vous a ete remis. Lisez-le bien, aventurier.");
    }

    private void sendWelcomeMessage(Player player) {
        player.sendMessage("");
        player.sendMessage("\u00a75\u00a7l* BIENVENUE SUR VOIDHEART *");
        player.sendMessage("\u00a77Le vide vous accueille, aventurier...");
        player.sendMessage("");
    }
}
