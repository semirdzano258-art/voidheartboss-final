package com.smp.voidheartboss;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class CustomItems {

    public static ItemStack createLarmeAncien() {
        ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("\u00a7b\u00a7l\u2726 Larme de l'Ancien \u2726");
        meta.setLore(Arrays.asList(
            "\u00a78\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac",
            "\u00a73\u2726 \u00a7lRelique des Profondeurs",
            "\u00a77Une larme cristallisee du",
            "\u00a77Gardien des Abysses.",
            "",
            "\u00a77Elle renferme le pouvoir",
            "\u00a77de l'ocean primordial...",
            "",
            "\u00a7e\u26a1 \u00a7lUtilisation :",
            "\u00a77Permet d'ouvrir les",
            "\u00a7f\u00a7nPortes des Enfers\u00a7r\u00a77.",
            "\u00a78\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac"
        ));
        meta.addEnchant(org.bukkit.enchantments.Enchantment.UNBREAKING, 1, true);
        meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createEssenceNeant() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("\u00a75\u00a7l\u2620 Essence du Neant \u2620");
        meta.setLore(Arrays.asList(
            "\u00a78\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac",
            "\u00a75\u2620 \u00a7lFragment de l'Obscurite",
            "\u00a77Un fragment de l'obscurite",
            "\u00a77absolue, arrache de l'ame",
            "\u00a77du Wither.",
            "",
            "\u00a77On dit que cette essence",
            "\u00a77peut ouvrir des portes",
            "\u00a77vers des dimensions",
            "\u00a77inconnues...",
            "",
            "\u00a7e\u26a1 \u00a7lUtilisation :",
            "\u00a77Permet d'activer le",
            "\u00a7f\u00a7nPortail de l'End\u00a7r\u00a77.",
            "\u00a78\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac\u25ac"
        ));
        meta.addEnchant(org.bukkit.enchantments.Enchantment.UNBREAKING, 1, true);
        meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isLarmeAncien(ItemStack item) {
        if (item == null || item.getType() != Material.PRISMARINE_CRYSTALS) return false;
        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return false;
        return item.getItemMeta().getDisplayName().contains("Larme de l'Ancien");
    }

    public static boolean isEssenceNeant(ItemStack item) {
        if (item == null || item.getType() != Material.NETHER_STAR) return false;
        if (!item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return false;
        return item.getItemMeta().getDisplayName().contains("Essence du Neant");
    }
}
