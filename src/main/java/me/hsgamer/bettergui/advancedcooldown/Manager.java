package me.hsgamer.bettergui.advancedcooldown;

import me.hsgamer.bettergui.lib.core.bukkit.config.BukkitConfig;
import me.hsgamer.bettergui.lib.core.bukkit.utils.MessageUtils;
import me.hsgamer.bettergui.lib.core.collections.map.CaseInsensitiveStringHashMap;
import me.hsgamer.bettergui.lib.core.config.Config;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class Manager {
    private static final Map<String, Cooldown> cooldownMap = new CaseInsensitiveStringHashMap<>();
    private static Config config;
    private static File folder;

    private Manager() {
        // EMPTY
    }

    public static void setConfig(Config config) {
        Manager.config = config;
    }

    public static void setFolder(File folder) {
        Manager.folder = folder;
    }

    public static void loadData() {
        config.getNormalizedValues(false).forEach((key, value) -> {
            Config dataConfig = new BukkitConfig(new File(folder, key + ".yml"));
            dataConfig.setup();
            Cooldown cooldown = new Cooldown(key, String.valueOf(value), dataConfig);
            cooldown.loadData();
            cooldownMap.put(key, cooldown);
        });
    }

    public static void saveData() {
        cooldownMap.values().forEach(Cooldown::saveData);
    }

    public static void clearData() {
        cooldownMap.clear();
    }

    public static boolean isInCooldown(String cooldownName, UUID uuid) {
        return Optional.ofNullable(cooldownMap.get(cooldownName))
                .map(cooldown -> cooldown.isInCooldown(uuid))
                .orElseGet(() -> {
                    Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(player -> MessageUtils.sendMessage(player, Main.COOLDOWN_NOT_FOUND.getValue()));
                    return true;
                });
    }

    public static void startCooldown(String cooldownName, UUID uuid) {
        if (cooldownMap.containsKey(cooldownName)) {
            cooldownMap.get(cooldownName).startCooldown(uuid);
        } else {
            Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(player -> MessageUtils.sendMessage(player, Main.COOLDOWN_NOT_FOUND.getValue()));
        }
    }

    public static long getCooldown(String cooldownName, UUID uuid) {
        return Optional.ofNullable(cooldownMap.get(cooldownName))
                .map(cooldown -> cooldown.getCooldown(uuid))
                .orElse(0L);
    }
}
